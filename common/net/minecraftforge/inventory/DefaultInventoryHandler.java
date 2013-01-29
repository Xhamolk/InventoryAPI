package net.minecraftforge.inventory;


import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import java.util.ArrayList;


/**
 * The default implementation of IInventoryHandler.
 * Designed to handler ISidedInventory, IDynamicInventory and the regular IInventory.
 */
public class DefaultInventoryHandler implements IInventoryHandler {


	public ArrayList<ItemStack> listItemsInInventory(IInventory inventory, ForgeDirection side) {
		int iMin = 0, iMax;
		if( inventory instanceof ISidedInventory ) {
			iMin = ((ISidedInventory) inventory).getStartInventorySide( side );
			iMax = iMin + ((ISidedInventory) inventory).getSizeInventorySide( side );
		} else {
			iMax = inventory.getSizeInventory();
		}

		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for( int slotIndex = iMin; slotIndex < iMax; slotIndex++ ) {
			ItemStack stackInSlot = inventory.getStackInSlot( slotIndex );
			if( stackInSlot != null )
				list.add( stackInSlot );
		}

		return list;
	}


	public boolean canPlaceItemOnInventory(IInventory inventory, ItemStack itemStack, ForgeDirection side, boolean fitAll) {
		int space = getSpaceInInventoryForItem( inventory, itemStack, side );
		return fitAll && space == itemStack.stackSize || space > 0;
	}

	public int addItemToInventory(IInventory inventory, ItemStack itemStack, ForgeDirection side) {
		int iMin = 0, iMax;
		if( inventory instanceof ISidedInventory ) {
			iMin = ((ISidedInventory) inventory).getStartInventorySide( side );
			iMax = iMin + ((ISidedInventory) inventory).getSizeInventorySide( side );
		} else {
			iMax = inventory.getSizeInventory();
		}

		int added = 0;
		for( int slotIndex = iMin; slotIndex < iMax; slotIndex++ ) {
			if( itemStack.stackSize == 0 )
				break;
			added += addItemToInventorySlot( inventory, slotIndex, itemStack );
		}
		return added;
	}

	public int addItemToInventorySlot(IInventory inventory, int slotIndex, ItemStack itemStack) {
		int space = getSpaceInSlotForItem( inventory, slotIndex, itemStack );
		if( space <= 0 )
			return 0;

		int amount = Math.min( space, itemStack.stackSize );
		ItemStack inventoryStack = inventory.getStackInSlot( slotIndex );
		ItemStack placedStack;

		if( inventoryStack == null ) {
			placedStack = itemStack.splitStack( amount );
			inventory.setInventorySlotContents( slotIndex, placedStack );
		} else {
			placedStack = itemStack.copy();
			placedStack.stackSize = amount;

			inventoryStack.stackSize += amount;
			itemStack.stackSize -= amount;
		}

		if( inventory instanceof IDynamicInventory )
			((IDynamicInventory) inventory).onItemPlaced( itemStack, slotIndex );

		inventory.onInventoryChanged();
		return amount;
	}

	public int getSpaceInInventoryForItem(IInventory inventory, ItemStack itemStack, ForgeDirection side) {
		int iMin = 0, iMax;
		if( inventory instanceof ISidedInventory ) {
			iMin = ((ISidedInventory) inventory).getStartInventorySide( side );
			iMax = iMin + ((ISidedInventory) inventory).getSizeInventorySide( side );
		} else {
			iMax = inventory.getSizeInventory();
		}

		int space = 0;
		for( int slotIndex = iMin; slotIndex < iMax; slotIndex++ ) {
			space += getSpaceInSlotForItem( inventory, slotIndex, itemStack );
		}
		return space;
	}

	public int getSpaceInSlotForItem(IInventory inventory, int slotIndex, ItemStack itemStack) {
		try {
			if( inventory instanceof IDynamicInventory )
				return ((IDynamicInventory) inventory).getSlotCapacityForItem( itemStack, slotIndex );

			ItemStack stackInSlot = inventory.getStackInSlot( slotIndex );
			if( stackInSlot == null )
				return inventory.getInventoryStackLimit();

			if( !InventoryUtils.areItemStacksSimilar( stackInSlot, itemStack ) )
				return 0;

			int maxSize = Math.min( stackInSlot.getMaxStackSize(), inventory.getInventoryStackLimit() );
			int space = maxSize - stackInSlot.stackSize;
			return space < 0 ? 0 : space;
		} catch ( ArrayIndexOutOfBoundsException aiob ) {
			return 0;
		} catch ( IndexOutOfBoundsException iob ) {
			return 0;
		}
	}

	@Override
	public ItemStack takeItemFromInventory(IInventory inventory, ForgeDirection side) {
		int iMin = 0, iMax;
		if( inventory instanceof ISidedInventory ) {
			iMin = ((ISidedInventory) inventory).getStartInventorySide( side );
			iMax = iMin + ((ISidedInventory) inventory).getSizeInventorySide( side );
		} else {
			iMax = inventory.getSizeInventory();
		}

		for( int slotIndex = iMin; slotIndex < iMax; slotIndex++ ) {
			int count = getItemCountInSlot( inventory, slotIndex );
			if( count > 0 ) {
				return takeItemFromInventorySlot( inventory, slotIndex );
			}
		}

		return null;
	}


	public ItemStack takeItemFromInventory(IInventory inventory, ItemStack item, ForgeDirection side) {
		return takeItemFromInventory( inventory, item, item.stackSize, side );
	}

	public ItemStack takeItemFromInventory(IInventory inventory, ItemStack item, int quantity, ForgeDirection side) {
		int iMin = 0, iMax;
		if( inventory instanceof ISidedInventory ) {
			iMin = ((ISidedInventory) inventory).getStartInventorySide( side );
			iMax = iMin + ((ISidedInventory) inventory).getSizeInventorySide( side );
		} else {
			iMax = inventory.getSizeInventory();
		}

		int remaining = Math.min( quantity, item.getMaxStackSize() );
		ItemStack takenItem = null;
		for( int slotIndex = iMin; slotIndex < iMax; slotIndex++ ) {
			if( remaining <= 0 )
				break;

			int count = getItemCountInSlot( inventory, slotIndex, item );
			if( count == 0 )
				continue;

			ItemStack tempStack = takeItemFromInventorySlot( inventory, slotIndex, remaining );
			if( tempStack == null )
				continue;

			remaining -= tempStack.stackSize;

			if( takenItem == null )
				takenItem = tempStack;
			else
				takenItem.stackSize += tempStack.stackSize;
		}

		return takenItem;
	}


	// takes the entire stack in the slot.
	public ItemStack takeItemFromInventorySlot(IInventory inventory, int slotIndex) {
		return takeItemFromInventorySlot( inventory, slotIndex, Integer.MAX_VALUE );
	}


	// will manipulate the inventoryStack's stack size
	// might return less than requested, unless there is plenty to fulfill the request.
	// prefer: takeItemFromInventory instead
	public ItemStack takeItemFromInventorySlot(IInventory inventory, int slotIndex, int quantity) {
		try {

			boolean dynamicInventory = inventory instanceof IDynamicInventory;
			if( dynamicInventory ) {
				int available = ((IDynamicInventory) inventory).getItemAvailabilityInSlot( slotIndex );
				if( available > 0 )
					quantity = Math.min( available, quantity );
				else
					return null;
			}

			ItemStack inventoryStack = inventory.getStackInSlot( slotIndex );
			if( inventoryStack == null )
				return null;

			ItemStack itemTaken;
			if( quantity > inventoryStack.stackSize ) {
				itemTaken = inventoryStack.copy();
				inventory.setInventorySlotContents( slotIndex, null );
			} else {
				itemTaken = inventory.decrStackSize( slotIndex, quantity );
				if( inventoryStack.stackSize == 0 )
					inventory.setInventorySlotContents( slotIndex, null );
			}

			if( dynamicInventory ) {
				((IDynamicInventory) inventory).onItemTaken( itemTaken, slotIndex );
			}

			inventory.onInventoryChanged();
			return itemTaken;
		} catch ( ArrayIndexOutOfBoundsException aiob ) {
			return null;
		} catch ( IndexOutOfBoundsException iob ) {
			return null;
		}
	}

	public int getItemCountInInventory(IInventory inventory, ItemStack itemStack, ForgeDirection side) {
		int iMin = 0, iMax;
		if( inventory instanceof ISidedInventory ) {
			iMin = ((ISidedInventory) inventory).getStartInventorySide( side );
			iMax = iMin + ((ISidedInventory) inventory).getSizeInventorySide( side );
		} else {
			iMax = inventory.getSizeInventory();
		}

		int count = 0;
		for( int slotIndex = iMin; slotIndex < iMax; slotIndex++ ) {
			count += getItemCountInSlot( inventory, slotIndex, itemStack );
		}
		return count;
	}

	@Override
	public int getItemCountInSlot(IInventory inventory, int slotIndex) {
		try {
			if( inventory instanceof IDynamicInventory )
				return ((IDynamicInventory) inventory).getItemAvailabilityInSlot( slotIndex );

			ItemStack stackInSlot = inventory.getStackInSlot( slotIndex );
			if( stackInSlot == null )
				return 0;

			return stackInSlot.stackSize;
		} catch ( ArrayIndexOutOfBoundsException aiob ) {
			return 0;
		} catch ( IndexOutOfBoundsException iob ) {
			return 0;
		}
	}

	public int getItemCountInSlot(IInventory inventory, int slotIndex, ItemStack itemStack) {
		try {
			ItemStack stackInSlot = inventory.getStackInSlot( slotIndex );

			if( inventory instanceof IDynamicInventory ) {
				int available = ((IDynamicInventory) inventory).getItemAvailabilityInSlot( slotIndex );
				if( itemStack != null && !InventoryUtils.areItemStacksSimilar( stackInSlot, itemStack ) )
					return 0;
				return available;
			}

			if( stackInSlot == null )
				return 0;

			if( itemStack != null && InventoryUtils.areItemStacksSimilar( itemStack, stackInSlot ) )
				return stackInSlot.stackSize;

			return 0;
		} catch ( ArrayIndexOutOfBoundsException aiob ) {
			return 0;
		} catch ( IndexOutOfBoundsException iob ) {
			return 0;
		}
	}

}
