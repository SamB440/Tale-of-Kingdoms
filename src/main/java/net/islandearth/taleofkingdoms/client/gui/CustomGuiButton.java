package net.islandearth.taleofkingdoms.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class CustomGuiButton extends GuiButton {
	
	private boolean closeOnClick;
	private ButtonCommand onClick;
	
	public CustomGuiButton(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, 200, 20, buttonText);
	}

	public CustomGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}
	
	public CustomGuiButton closeOnClick() {
		this.closeOnClick = !this.closeOnClick;
		return this;
	}
	
	public CustomGuiButton onClick(ButtonCommand onClick) {
		this.onClick = onClick;
		return this;
	}
	
	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		if (this.closeOnClick) Minecraft.getInstance().player.closeScreen();
		if (this.onClick != null) onClick.run();
	}
}
