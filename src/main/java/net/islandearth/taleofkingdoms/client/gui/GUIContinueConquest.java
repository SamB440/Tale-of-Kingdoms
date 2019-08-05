package net.islandearth.taleofkingdoms.client.gui;

import java.util.Arrays;

import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;

public class GUIContinueConquest extends GuiScreen {

	// Labels
	private GuiLabel label;
	
	// Buttons
	private GuiButton mButtonClose;
	
	// Other
	private ConquestInstance instance;
	
	public GUIContinueConquest(ConquestInstance instance) {
		this.instance = instance;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttons.clear();
		this.labels.clear();
		//this.labels.add(label = new GuiLabel(Arrays.asList(Minecraft.getInstance().player.getName() + ", your conquest, " + instance.getName() + ", has come far.",
				//"Now you seek to venture further, and continue your journey.",
				//"Safe travels, and go forth!"), 0, fontRenderer));
		//this.labels.add(label = new GuiLabel(fontRenderer, 1, this.width / 2 - 150, this.height / 2 + 40, 300, 20, 0xFFFFFF));
		this.buttons.add(mButtonClose = new CustomGuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) + 10, 1, 1, "Continue your Conquest.").closeOnClick());
		
	}
	
	@Override
	public void render(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        super.render(par1, par2, par3);
    }
	
	@Override
    public boolean doesGuiPauseGame() {
        return true;
    }
	
	@Override
	public boolean allowCloseWithEscape() {
	      return false;
	}
}
