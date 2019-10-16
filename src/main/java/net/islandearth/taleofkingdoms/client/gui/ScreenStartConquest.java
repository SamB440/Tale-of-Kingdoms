package net.islandearth.taleofkingdoms.client.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.islandearth.taleofkingdoms.schematic.OperationInstance;
import net.islandearth.taleofkingdoms.schematic.Operations;
import net.islandearth.taleofkingdoms.schematic.Schematic;
import net.islandearth.taleofkingdoms.schematic.SchematicHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ScreenStartConquest extends ScreenTOK {
	
	// Buttons
	private Button mButtonClose;
	
	// Text fields
	private TextFieldWidget text;
	
	// Other
	private String worldName;
	private File toSave;
	private PlayerEntity player;
	private boolean loading;
	
	public ScreenStartConquest(String worldName, File toSave, PlayerEntity player) {
		this.worldName = worldName;
		this.toSave = toSave;
		this.player = player;
	}
	
	@Override
	public void init() {
		super.init();
		this.buttons.clear();
		this.text = new TextFieldWidget(this.font, this.width / 2 - 150, this.height / 2 - 40, 300, 20, "Sir Punchwood");
		this.addButton(mButtonClose = new Button(this.width / 2 - 100, this.height / 2 + 30, 200, 20, "Start your Conquest.", (button) -> {
			if (loading) return;
			ConquestInstance instance = new ConquestInstance(worldName, text.getText(), 0);
			try (Writer writer = new FileWriter(toSave)) {
			    Gson gson = new GsonBuilder().setPrettyPrinting().create();
			    gson.toJson(instance, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().addConquest(worldName, instance, true);
			button.setMessage("Loading, please wait...");
			this.loading = true;
			// Load guild castle schematic
			OperationInstance oi = SchematicHandler.pasteSchematic(Schematic.GUILD_CASTLE, player);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Minecraft.getInstance().runImmediately(() -> {
						try {
							int progress = Operations.getProgress(oi.getOperationId());
							int blocksDone = progress == 0 ? 0 : (oi.getBlocks() / (100 / progress));
							button.setMessage("Loading, please wait... (" + Operations.getProgress(oi.getOperationId()) + "%, " + blocksDone + "/" + oi.getBlocks() + ")");
							if (progress >= 100) {
								button.setMessage("Reloading chunks...");
								minecraft.worldRenderer.loadRenderers();
								
								Timer timer2 = new Timer();
								timer2.schedule(new TimerTask() {
									@Override
									public void run() {
										Minecraft.getInstance().runImmediately(() -> {
											onClose();
											loading = false;
										});
									}
								}, 2000);
								this.cancel();
							}
						} catch (Exception e) {
							button.setMessage("Error: " + e.getCause().getMessage());
							e.printStackTrace();
						}
					});
				}
			}, 0, 10);
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
        this.drawCenteredString(this.font, "The Great Tides of Darkness are coming. Build your forces and vanquish evil.", this.width / 2, this.height / 2, 0xFFFFFF);
        this.drawCenteredString(this.font, "Be the hero you were born for. The Guild will prepare you...", this.width / 2, this.height / 2 + 10, 0xFFFFFF);
        this.text.render(par1, par2, par3);
        super.render(par1, par2, par3);
    }
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	protected List<Chunk> getChunksAroundPlayer(PlayerEntity player) {
		int[] offset = {-1,0,1};

		World world = player.world;
		int ox = player.chunkCoordX;
		int oz = player.chunkCoordZ;

		List<Chunk> chunks = new ArrayList<Chunk>();
		for (int x : offset) {
			for (int z : offset) {
				Chunk chunk = world.getChunk(ox + x, oz + z);
				chunks.add(chunk);
			}
		} return chunks;
	}
}
