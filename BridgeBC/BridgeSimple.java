import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.inventory.DefaultInventoryHandler;


// Covers TransactorSimple and TransactorSided
public class BridgeSimple extends DefaultInventoryHandler {


	// Difference with the default:
	// First tries to insert to a partial slot, then to the first empty slot.
	public int addItemToInventory(IInventory inventory, ItemStack itemStack, ForgeDirection side) {
		int iMin = 0, iMax;
		if( inventory instanceof ISidedInventory ) {
			iMin = ((ISidedInventory) inventory).getStartInventorySide( side );
			iMax = iMin + ((ISidedInventory) inventory).getSizeInventorySide( side );
		} else {
			iMax = inventory.getSizeInventory();
		}

		int added = 0;
		int slot = iMin;

		// Add to a slot that has some of that item.
		while( (slot = getPartialSlot( inventory, itemStack, slot, iMax )) >= 0 ) {
			if( itemStack.stackSize <= 0 )
				return added;

			added += addItemToInventorySlot( inventory, slot, itemStack );
		}
		slot = iMin;

		// Add to an empty slot.
		while( (slot = getEmptySlot( inventory, itemStack, slot, iMax )) >= 0 ) {
			if( itemStack.stackSize <= 0 )
				return added;

			added += addItemToInventorySlot( inventory, slot, itemStack );
		}

		return added;
	}

	protected int getPartialSlot(IInventory inventory, ItemStack itemStack, int minIndex, int maxIndex) {
		for( int index = minIndex; index < maxIndex; index++ ) {
			if( inventory.getStackInSlot( index ) == null )
				continue;

			if( getSpaceInSlotForItem( inventory, index, itemStack ) > 0 )
				return index;
		}
		return -1;
	}

	protected int getEmptySlot(IInventory inventory, ItemStack itemStack, int minIndex, int maxIndex) {
		for( int index = minIndex; index < maxIndex; index++ ) {
			if( inventory.getStackInSlot( index ) == null && getSpaceInSlotForItem( inventory, index, itemStack ) > 0 )
				return index;
		}
		return -1;
	}

}
