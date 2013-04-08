package xk.xact.core.items;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import xk.xact.XActMod;
import xk.xact.util.Textures;

import java.util.List;

public class ItemBlueprint extends ItemMap { // its a "map" for simplicity reasons.

	public ItemBlueprint(int itemID) {
		super( itemID );
		this.setUnlocalizedName( "blueprint" );
		this.setMaxStackSize( 1 );
		this.setCreativeTab( XActMod.xactTab );
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		list.add( "it's something!" );
	}

	@Override
	@SideOnly(Side.CLIENT) // Item's Texture
	public void updateIcons(IconRegister iconRegister) {
		this.iconIndex = iconRegister.registerIcon( Textures.ITEM_BLUEPRINT );
	}

	@Override
	public MapData getMapData(ItemStack itemStack, World world) {
		return null; // this is not really a map.
	}

	@Override
	public Packet createMapDataPacket(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		return null; // no need to send updates to the client.
	}

	@Override
	public void onUpdate(ItemStack item, World world, Entity entity, int slotNum, boolean isHeld) {
		// is there something we need to do every tick?
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		// what to do when created? well.. i could init the NBT.
		itemStack.setTagCompound( new NBTTagCompound() );
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		int blockID = world.getBlockId( x, y, z );
		if( blockID == Block.workbench.blockID ) {
			if( world.isRemote ) // client-side only
				player.openGui( XActMod.instance, 4, world, x, y, z );
			return true;
		}
		return false;
	}

}
