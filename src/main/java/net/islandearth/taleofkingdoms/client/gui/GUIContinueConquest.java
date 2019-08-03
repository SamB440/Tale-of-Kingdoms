package net.islandearth.taleofkingdoms.client.gui;

import java.io.IOException;

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
		this.buttonList.clear();
		this.labelList.clear();
		this.labelList.add(label = new GuiLabel(fontRenderer, 1, this.width / 2 - 150, this.height / 2 + 40, 300, 20, 0xFFFFFF));
		this.buttonList.add(mButtonClose = new GuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) + 10, "Continue your Conquest."));
        label.addLine(Minecraft.getMinecraft().player.getName() + ", your conquest, " + instance.getName() + ", has come far.");
        label.addLine("Now you seek to venture further, and continue your journey.");
        label.addLine("Safe travels, and go forth!");
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        super.drawScreen(par1, par2, par3);
    }
	
	@Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == mButtonClose) mc.player.closeScreen();
    }
	
	@Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
