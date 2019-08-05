package net.islandearth.taleofkingdoms.client.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.util.InputMappings;

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
		this.buttons.clear();
		this.labels.clear();
		//this.labels.add(label = new CustomGuiLabel(Arrays.asList("Enter the name of your Conquest, and click done to begin your journey..."), 0xFFFFFF, fontRenderer));
		//this.labels.add(label = new GuiLabel(fontRenderer, 1, this.width / 2 - 150, this.height / 2 + 40, 300, 20, 0xFFFFFF));
		this.buttons.add(mButtonClose = new CustomGuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) + 10, "Start new Conquest.").onClick(() -> {
			ConquestInstance instance = new ConquestInstance(worldName, text.getText(), 0);
			try (Writer writer = new FileWriter(toSave)) {
			    Gson gson = new GsonBuilder().setPrettyPrinting().create();
			    gson.toJson(instance, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}));
		this.text = new GuiTextField(2, fontRenderer, this.width / 2 - 68, this.height/2-46, 137, 20);
        text.setMaxStringLength(32);
        text.setText("Name your Kingdom");
        //this.text.setFocused(true);
	}
	
	/*@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {       
        this.text.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);         
        if (p_keyPressed_2_ == 69 && !this.text.isFocused()) {
			return false;
        } else return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }*/
	
	/*@Override
	public void render(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        super.render(par1, par2, par3);
        this.text.setVisible(true);
    }*/
	
	/*@Override
	public boolean mouseClicked(double x, double y, int btn) {
        this.text.mouseClicked(x, y, btn);
        return super.mouseClicked(x, y, btn);
    }*/
	
	@Override
    public boolean doesGuiPauseGame() {
        return true;
    }
	
	@Override
	public boolean allowCloseWithEscape() {
		return false;
	}
}
