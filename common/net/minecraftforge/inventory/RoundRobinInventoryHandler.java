package net.minecraftforge.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;


public class RoundRobinInventoryHandler extends DefaultInventoryHandler {

	public int getSpaceInSlotForItem(IInventory inventory, int slotIndex, ItemStack itemStack) {
		try {
			if( inventory instanceof IDynamicInventory )
				return ((IDynamicInventory) inventory).getSlotCapacityForItem( itemStack, slotIndex );

			ItemStack stackInSlot = inventory.getStackInSlot( slotIndex );
			if( stackInSlot == null )
				return 0; // Don't place items on empty slots.

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

	public int addItemToInventory(IInventory inventory, ItemStack stack, ForgeDirection side) {
		int added = 0;

		for( int itemLoop = 0; itemLoop < stack.stackSize; itemLoop++ ) {

			int minSimilar = Integer.MAX_VALUE;
			int targetSlot = -1;

			for( int index = 0; index < inventory.getSizeInventory() && minSimilar > 1; ++index ) {
				ItemStack stackInInventory = inventory.getStackInSlot( index );

				if( stackInInventory == null )
					continue;

				int maxStackSize = Math.min( stackInInventory.getMaxStackSize(), inventory.getInventoryStackLimit() );
				if( stackInInventory.stackSize >= maxStackSize || !InventoryUtils.areItemStacksSimilar( stackInInventory, stack ) )
					continue;

				if( stackInInventory.stackSize > 0 && stackInInventory.stackSize < minSimilar ) {
					minSimilar = stackInInventory.stackSize;
					targetSlot = index;
				}
			}

			if( targetSlot != -1 ) {
				ItemStack itemToAdd = stack.stackSize > 1 ? stack.splitStack( 1 ) : stack;

				added += this.addItemToInventorySlot( inventory, targetSlot, itemToAdd );
			} else {
				break;
			}
		}

		return added;
	}

}
