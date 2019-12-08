package net.islandearth.taleofkingdoms.client.gui;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Find out more from our GUI research:
 * https://forum.islandearth.net/d/10-forge-modding-tutorial-1-14-creating-custom-guis-1-3
 */
@OnlyIn(Dist.CLIENT)
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
		TaleOfKingdoms.LOGGER.info("Binding texture: " + image.getPath());
		Minecraft.getInstance().getTextureManager().bindTexture(image);
	}
}
