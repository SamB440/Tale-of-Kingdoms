package net.islandearth.taleofkingdoms.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

/**
 * Find out more from our GUI research:
 * https://forum.islandearth.net/d/10-forge-modding-tutorial-1-14-creating-custom-guis-1-3
 */
public abstract class ScreenTOK extends Screen {
	
	protected ScreenTOK() {
		super(null);
	}
	
	@Override
	public String getNarrationMessage() {
		return "";
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	
	@Override
	public boolean isPauseScreen() {
		return true;
	}
	
	public void addImage(ResourceLocation image) {
		ITextureObject texture = Minecraft.getInstance().getTextureManager().getTexture(image);
		texture.bindTexture();
	}
}
