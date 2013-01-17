package net.minecraftforge.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;

public final class InventoryUtils {

	private InventoryUtils() {}

	/**
	 * The default IInventoryHandler.
	 */
	public static final IInventoryHandler defaultInventoryHandler = new DefaultInventoryHandler();

	/**
	 * Determines if two ItemStack contain the same kind of items.
	 * Compares itemIDs, damage value and the item's NBT. Disregards the stack size.
	 *
	 * @return true if both stacks match their itemIDs, damage value and NBT, or if both are null.
	 */
	public static boolean areItemStacksSimilar(ItemStack itemStack1, ItemStack itemStack2) {
		if( itemStack1 == null || itemStack2 == null )
			return itemStack1 == itemStack2; // both are null.

		if( itemStack1.itemID == itemStack2.itemID ) {
			if( itemStack2.getItemDamage() == itemStack1.getItemDamage() )
				return ItemStack.areItemStackTagsEqual( itemStack1, itemStack2 );
		}
		return false;
	}

	/**
	 * Gets the IInventoryHandler suitable for the passed IInventory.
	 *
	 * If <code>inventory</code> is not an ICustomInventory, the default IInventoryHandler will be returned.
	 * @see InventoryUtils#defaultInventoryHandler
	 */
	public static IInventoryHandler getInventoryHandler(IInventory inventory) {
		if( inventory == null )
			throw new IllegalArgumentException("Inventory Utils: inventory null");

		if( inventory instanceof ICustomInventory )
			return ((ICustomInventory) inventory).getInventoryHandler();
		return defaultInventoryHandler;
	}


	/**
	 * Whether if an ItemStack can be placed on the IInventory.
	 *
	 * @param inventory the IInventory where to place the item.
	 * @param itemStack the ItemStack to check
	 * @param side the ForgeDirection (side) from which the Inventory is accessed.
	 *             Note: This is ignored if <code>inventory</code> it not a <code>ISidedInventory</code>
	 * @param fitAll if true, this will check if there is enough space to fit the entire stack.
	 *               otherwise, if there is space for at least one item.
	 * @return fitAll && space == itemStack.stackSize || space > 0
	 */
	public static boolean canPlaceItemOnInventory(IInventory inventory, ItemStack itemStack, ForgeDirection side, boolean fitAll) {
		return getInventoryHandler( inventory ).canPlaceItemOnInventory( inventory, itemStack, side, fitAll );
	}

	/**
	 * Tries to add the ItemStack into the IInventory.
	 *
	 * Note: the itemStack.stackSize will be manipulated.
	 * If it's fully added, it's stack size will become 0.
	 *
	 * @param inventory the IInventory where to place the item.
	 * @param itemStack the ItemStack to add into the inventory
	 * @param side the ForgeDirection (side) from which the Inventory is accessed.
	 *             Note: This is ignored if <code>inventory</code> it not a <code>ISidedInventory</code>
	 * @return the amount of itemStack added into the inventory.
	 */
	public static int addItemToInventory(IInventory inventory, ItemStack itemStack, ForgeDirection side) {
		return getInventoryHandler( inventory ).addItemToInventory( inventory, itemStack, side );
	}

	/**
	 * Tries to add the ItemStack into the inventory slot.
	 *
	 * Prefer using <code>addItemToInventory</code>
	 *
	 * Note: the itemStack.stackSize will be manipulated.
	 * If it's fully added, it's stack size will become 0.
	 *
	 * @param inventory the IInventory where to place the item.
	 * @param slotIndex the inventory slot where to place the item.
	 * @param itemStack the ItemStack to add into the inventory
	 * @return the amount of itemStack added into the inventory.
	 * @see InventoryUtils#addItemToInventory(net.minecraft.inventory.IInventory, net.minecraft.item.ItemStack, net.minecraftforge.common.ForgeDirection)
	 */
	public static int addItemToInventorySlot(IInventory inventory, int slotIndex, ItemStack itemStack) {
		return getInventoryHandler( inventory ).addItemToInventorySlot( inventory, slotIndex, itemStack );
	}

	/**
	 * Gets the available space on the inventory for the itemStack.
	 * Will respect ISidedInventory's functionality.
	 *
	 * @param inventory the IInventory to check for space
	 * @param itemStack the ItemStack to compare
	 * @param side the ForgeDirection (side) from which the Inventory is accessed.
	 *             Note: This is ignored if <code>inventory</code> it not a <code>ISidedInventory</code>
	 * @return the amount of itemStack that could fit on the inventory.
	 */
	public static int getSpaceInInventoryForItem(IInventory inventory, ItemStack itemStack, ForgeDirection side) {
		return getInventoryHandler( inventory ).getSpaceInInventoryForItem( inventory, itemStack, side );
	}

	/**
	 * Gets the available space for the itemStack a the particular slot on the inventory provided.
	 * The returned value is the amount of itemStack that could be placed on that slot,
	 * but it's not limited to the current itemStack.stackSize.
	 *
	 * Note: will return 0 if a IndexOutOfBoundsException is caught.
	 *
	 * @param inventory the IInventory to check for space
	 * @param slotIndex the inventory slot to check for space
	 * @param itemStack the ItemStack to compare
	 * @return the amount of itemStack that could fit on the inventory slot.
	 */
	public static int getSpaceInSlotForItem(IInventory inventory, int slotIndex, ItemStack itemStack) {
		return getInventoryHandler( inventory ).getSpaceInSlotForItem( inventory, slotIndex, itemStack );
	}


	public static ItemStack takeItemFromInventory(IInventory inventory, ItemStack item, ForgeDirection side) {
		return getInventoryHandler( inventory ).takeItemFromInventory(inventory, item, side);
	}

	public static ItemStack takeItemFromInventory(IInventory inventory, ItemStack item, int quantity, ForgeDirection side) {
		return getInventoryHandler( inventory ).takeItemFromInventory( inventory, item, quantity, side );
	}


	public static ItemStack takeItemFromInventorySlot(IInventory inventory, int slotIndex) {
		return getInventoryHandler( inventory ).takeItemFromInventorySlot(inventory, slotIndex);
	}

	public static ItemStack takeItemFromInventorySlot(IInventory inventory, int slotIndex, int quantity) {
		return getInventoryHandler( inventory ).takeItemFromInventorySlot( inventory, slotIndex, quantity );
	}

	public static int getItemCountInInventory(IInventory inventory, ItemStack itemStack, ForgeDirection side) {
		return getInventoryHandler( inventory ).getItemCountInInventory( inventory, itemStack, side );
	}

	public static int getItemCountInSlot(IInventory inventory, int slotIndex, ItemStack itemStack) {
		return getInventoryHandler( inventory ).getItemCountInSlot( inventory, slotIndex, itemStack );
	}

	public static ArrayList<ItemStack> listItemsInInventory(IInventory inventory, ForgeDirection side) {
		return getInventoryHandler( inventory ).listItemsInInventory( inventory, side );
	}

}
