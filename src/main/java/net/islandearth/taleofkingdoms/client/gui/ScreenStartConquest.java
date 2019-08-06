package net.islandearth.taleofkingdoms.client.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.islandearth.taleofkingdoms.schematic.Schematic;
import net.islandearth.taleofkingdoms.schematic.SchematicHandler;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;

public class ScreenStartConquest extends ScreenTOK {
	
	// Buttons
	private Button mButtonClose;
	
	// Text fields
	private TextFieldWidget text;
	
	// Other
	private String worldName;
	private File toSave;
	private PlayerEntity player;
	
	public ScreenStartConquest(String worldName, File toSave, PlayerEntity player) {
		this.worldName = worldName;
		this.toSave = toSave;
		this.player = player;
	}
	
	@Override
	public void init() {
		super.init();
		this.buttons.clear();
		this.text = new TextFieldWidget(this.font, this.width / 2 - 150, this.height / 2 + 70, 300, 20, "Sir Punchwood");
		this.addButton(mButtonClose = new Button(this.width / 2 - 100, this.height - (this.height / 4) + 5, 200, 20, "Start new Conquest.", (button) -> {
			ConquestInstance instance = new ConquestInstance(worldName, text.getText(), 0);
			try (Writer writer = new FileWriter(toSave)) {
			    Gson gson = new GsonBuilder().setPrettyPrinting().create();
			    gson.toJson(instance, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().addConquest(worldName, instance, true);
			// Load guild castle schematic
			SchematicHandler.pasteSchematic(Schematic.GUILD_CASTLE, player);
			this.onClose();
		}));
		this.text.setMaxStringLength(32);
		this.text.setText("Sir Punchwood");
		this.text.setCanLoseFocus(false);
	    this.text.changeFocus(true);
		this.text.setEnabled(true);
		this.text.setFocused2(true);
		this.children.add(this.text);
	}
	
	@Override
	public void render(int par1, int par2, float par3) {
        this.renderBackground();
        this.drawCenteredString(this.font, "Enter the name of your Conquest, and click done to begin your journey...", this.width / 2, this.height / 2 + 50, 0xFFFFFF);
        this.text.render(par1, par2, par3);
        super.render(par1, par2, par3);
    }
}
