import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.inventory.DefaultInventoryHandler;
import net.minecraftforge.inventory.InventoryUtils;


// Covers TransactorRoundRobin
public class BridgeRoundRobin extends DefaultInventoryHandler {

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
