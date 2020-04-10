package net.islandearth.taleofkingdoms.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Find out more from our GUI research:
 * https://forum.islandearth.net/d/10-forge-modding-tutorial-1-14-creating-custom-guis-1-3
 */
@OnlyIn(Dist.CLIENT)
public abstract class ScreenTOK extends Screen {
	
	protected ScreenTOK(String title) {
		super(new StringTextComponent(title));
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	
	@Override
	public boolean isPauseScreen() {
		return true;
	}
}
