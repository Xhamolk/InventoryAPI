package net.minecraftforge.inventory;


import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;

public interface IInventoryHandler {

	public ArrayList<ItemStack> listItemsInInventory(IInventory inventory, ForgeDirection side);

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
	public boolean canPlaceItemOnInventory(IInventory inventory, ItemStack itemStack, ForgeDirection side, boolean fitAll);

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
	public int addItemToInventory(IInventory inventory, ItemStack itemStack, ForgeDirection side);

	public int addItemToInventorySlot(IInventory inventory, int slotIndex, ItemStack itemStack);

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
	public int getSpaceInInventoryForItem(IInventory inventory, ItemStack itemStack, ForgeDirection side);

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
	public int getSpaceInSlotForItem(IInventory inventory, int slotIndex, ItemStack itemStack);

	public ItemStack takeItemFromInventory(IInventory inventory, ItemStack item, ForgeDirection side);

	public ItemStack takeItemFromInventory(IInventory inventory, ItemStack item, int quantity, ForgeDirection side);

	public ItemStack takeItemFromInventorySlot(IInventory inventory, int slotIndex);

	// will manipulate the inventoryStack's stack size
	// might return less than requested, unless there is plenty to fulfill the request.
	// prefer: takeItemFromInventory instead
	public ItemStack takeItemFromInventorySlot(IInventory inventory, int slotIndex, int quantity);

	public int getItemCountInInventory(IInventory inventory, ItemStack itemStack, ForgeDirection side);

	public int getItemCountInSlot(IInventory inventory, int slotIndex, ItemStack itemStack);

}
