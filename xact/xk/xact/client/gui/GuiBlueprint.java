package xk.xact.client.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import xk.xact.client.button.ButtonItem;
import xk.xact.client.button.CustomButtons;
import xk.xact.util.CustomPacket;
import xk.xact.util.Textures;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static xk.xact.client.GuiUtils.itemRender;

public class GuiBlueprint extends GuiScreen {

	private final Object blueprint;

	public int guiLeft;
	public int guiTop;

	private int xSize = 256;
	private int ySize = 256;

	public GuiBlueprint(Object blueprint) {
		this.blueprint = blueprint;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;

		// Todo: add ALL the buttons.
		GuiButton button = CustomButtons.createItemButton( guiLeft + 30, guiTop + 30, (ItemStack) blueprint );
		button.id = 0;
		this.buttonList.add( button );
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		this.drawDefaultBackground();
		int k = this.guiLeft;
		int l = this.guiTop;

		this.drawGuiContainerBackgroundLayer( partialTick, mouseX, mouseY );

		GL11.glDisable( GL12.GL_RESCALE_NORMAL );
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable( GL11.GL_LIGHTING );
		GL11.glDisable( GL11.GL_DEPTH_TEST );
		super.drawScreen( mouseX, mouseY, partialTick );
		RenderHelper.enableGUIStandardItemLighting();

		GL11.glPushMatrix();
		GL11.glTranslatef( (float) k, (float) l, 0.0F );
		GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
		GL11.glEnable( GL12.GL_RESCALE_NORMAL );

		short short1 = 240;
		short short2 = 240;
		OpenGlHelper.setLightmapTextureCoords( OpenGlHelper.lightmapTexUnit, (float) short1 / 1.0F, (float) short2 / 1.0F );
		GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );


		this.drawGuiContainerForegroundLayer( mouseX, mouseY );

		InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
		ItemStack itemstack = inventoryplayer.getItemStack();

		if( itemstack != null ) {
			this.drawItemStack( itemstack, mouseX - this.guiLeft - 8, mouseY - this.guiTop - 8, null );
		}
		GL11.glPopMatrix();
		GL11.glEnable( GL11.GL_LIGHTING );
		GL11.glEnable( GL11.GL_DEPTH_TEST );
		RenderHelper.enableStandardItemLighting();
	}


	private void drawGuiContainerBackgroundLayer(float par3, int par1, int par2) {
		GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
		this.mc.renderEngine.bindTexture( Textures.BLUEPRINT_BG );
		int cornerX = (this.width - this.xSize) / 2;
		int cornerY = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect( cornerX, cornerY, 0, 0, this.xSize, this.ySize );
	}

	private void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// anything to do?
	}


	private void drawItemStack(ItemStack itemStack, int x, int y, String alternativeStackSize) {
		GL11.glTranslatef( 0.0F, 0.0F, 32.0F );
		this.zLevel = 200.0F;
		itemRender.zLevel = 200.0F;
		itemRender.renderItemAndEffectIntoGUI( this.fontRenderer, this.mc.renderEngine, itemStack, x, y );
		itemRender.renderItemStack( this.fontRenderer, this.mc.renderEngine, itemStack, x, y, alternativeStackSize );
		this.zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
	}

	protected void drawItemStackTooltip(ItemStack itemStack, int x, int y) {
		List list = itemStack.getTooltip( this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips );


		for( int k = 0; k < list.size(); ++k ) {
			if( k == 0 ) {
				list.set( k, "\u00a7" + Integer.toHexString( itemStack.getRarity().rarityColor ) + (String) list.get( k ) );
			} else {
				list.set( k, EnumChatFormatting.GRAY + (String) list.get( k ) );
			}
		}

		this.drawTooltipList( list, x, y );
	}


	protected void drawTooltipList(List list, int posX, int posY) {
		if( !list.isEmpty() ) {
			GL11.glDisable( GL12.GL_RESCALE_NORMAL );
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable( GL11.GL_LIGHTING );
			GL11.glDisable( GL11.GL_DEPTH_TEST );
			int k = 0;
			Iterator iterator = list.iterator();

			while( iterator.hasNext() ) {
				String s = (String) iterator.next();
				int l = this.fontRenderer.getStringWidth( s );

				if( l > k ) {
					k = l;
				}
			}

			int x = posX + 12;
			int y = posY - 12;
			int k1 = 8;

			if( list.size() > 1 ) {
				k1 += 2 + (list.size() - 1) * 10;
			}

			if( x + k > this.width ) {
				x -= 28 + k;
			}

			if( y + k1 + 6 > this.height ) {
				y = this.height - k1 - 6;
			}

			this.zLevel = 300.0F;
			itemRender.zLevel = 300.0F;
			int l1 = -267386864;
			this.drawGradientRect( x - 3, y - 4, x + k + 3, y - 3, l1, l1 );
			this.drawGradientRect( x - 3, y + k1 + 3, x + k + 3, y + k1 + 4, l1, l1 );
			this.drawGradientRect( x - 3, y - 3, x + k + 3, y + k1 + 3, l1, l1 );
			this.drawGradientRect( x - 4, y - 3, x - 3, y + k1 + 3, l1, l1 );
			this.drawGradientRect( x + k + 3, y - 3, x + k + 4, y + k1 + 3, l1, l1 );
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			this.drawGradientRect( x - 3, y - 3 + 1, x - 3 + 1, y + k1 + 3 - 1, i2, j2 );
			this.drawGradientRect( x + k + 2, y - 3 + 1, x + k + 3, y + k1 + 3 - 1, i2, j2 );
			this.drawGradientRect( x - 3, y - 3, x + k + 3, y - 3 + 1, i2, i2 );
			this.drawGradientRect( x - 3, y + k1 + 2, x + k + 3, y + k1 + 3, j2, j2 );

			for( int i = 0; i < list.size(); i++ ) {
				String s1 = (String) list.get( i );
				this.fontRenderer.drawStringWithShadow( s1, x, y, -1 );

				if( i == 0 ) {
					y += 2;
				}

				y += 10;
			}

			this.zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
			GL11.glEnable( GL11.GL_LIGHTING );
			GL11.glEnable( GL11.GL_DEPTH_TEST );
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable( GL12.GL_RESCALE_NORMAL );
		}
	}

	protected boolean isPointInRegion(int x1, int y1, int width, int height, int pointX, int pointY) {
		pointX -= this.guiLeft;
		pointY -= this.guiTop;
		return pointX >= x1 - 1 && pointX < x1 + width + 1 && pointY >= y1 - 1 && pointY < y1 + height + 1;
	}

	@Override
	protected void keyTyped(char key, int keyCode) {
		if( keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.keyCode ) {
			this.mc.thePlayer.closeScreen();
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		if( !this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead ) {
			this.mc.thePlayer.closeScreen();
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {

		if( button instanceof ButtonItem )

		// Open GuiRecipe.
		openGuiRecipe();
	}

	private void openGuiRecipe() {
		try {
			this.mc.thePlayer.closeScreen();
			this.mc.getNetHandler().addToSendQueue( CustomPacket.openGui( 5 ).toPacket() );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}

}
