package net.islandearth.taleofkingdoms.client.gui;

import net.minecraft.client.gui.screen.Screen;

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
}
