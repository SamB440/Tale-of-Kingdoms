package net.islandearth.taleofkingdoms.client.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.lwjgl.input.Keyboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GUIStartConquest extends GuiScreen {
	
	// Text fields
	private GuiTextField text;
	
	// Buttons
	private GuiButton mButtonClose;
	
	// Labels
	private GuiLabel label;
	
	// Other
	private String worldName;
	private File toSave;
	
	public GUIStartConquest(String worldName, File toSave) {
		this.worldName = worldName;
		this.toSave = toSave;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		this.labelList.clear();
		this.labelList.add(label = new GuiLabel(fontRenderer, 1, this.width / 2 - 150, this.height / 2 + 40, 300, 20, 0xFFFFFF));
		this.buttonList.add(mButtonClose = new GuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) + 10, "Start new Conquest."));
		this.text = new GuiTextField(2, fontRenderer, this.width / 2 - 68, this.height/2-46, 137, 20);
        text.setMaxStringLength(32);
        text.setText("Name your Kingdom");
        this.text.setFocused(true);
        label.addLine("Enter the name of your Conquest, and click done to begin your journey...");
	}
	
	@Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == mButtonClose) {
        	ConquestInstance instance = new ConquestInstance(worldName, text.getText(), 0);
			try (Writer writer = new FileWriter(toSave)) {
			    Gson gson = new GsonBuilder().setPrettyPrinting().create();
			    gson.toJson(instance, writer);
			}
            mc.player.closeScreen();
        }
    }
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException {       
        this.text.textboxKeyTyped(par1, par2);         
        if (par2 == Keyboard.KEY_E && !this.text.isFocused()) {
			super.keyTyped(par1, par2);
        }
    }
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.text.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
	
	@Override
	protected void mouseClicked(int x, int y, int btn) throws IOException {
		super.mouseClicked(x, y, btn);
        this.text.mouseClicked(x, y, btn);
    }
	
	@Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
