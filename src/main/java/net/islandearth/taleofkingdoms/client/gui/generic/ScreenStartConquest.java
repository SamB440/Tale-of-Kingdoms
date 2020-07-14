package net.islandearth.taleofkingdoms.client.gui.generic;

import com.google.gson.Gson;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.forge.ForgeAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.islandearth.taleofkingdoms.common.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.schematic.OperationInstance;
import net.islandearth.taleofkingdoms.common.schematic.Operations;
import net.islandearth.taleofkingdoms.common.schematic.Schematic;
import net.islandearth.taleofkingdoms.common.schematic.SchematicHandler;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;

public class ScreenStartConquest extends ScreenTOK {
	
	// Buttons
	private Button mButtonClose;
	
	// Text fields
	private TextFieldWidget text;
	
	// Other
	private final String worldName;
	private final File toSave;
	private final PlayerEntity player;
	private boolean loading;
	
	public ScreenStartConquest(String worldName, File toSave, PlayerEntity player) {
		super("taleofkingdoms.menu.startconquest.name");
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

			button.setMessage("Loading, please wait...");
			// Load guild castle schematic
			OperationInstance oi = SchematicHandler.pasteSchematic(Schematic.GUILD_CASTLE, player);
			BlockVector3 max = oi.getRegion().getMaximumPoint();
			BlockVector3 min = oi.getRegion().getMinimumPoint();
			BlockPos start = new BlockPos(max.getBlockX(), max.getBlockY(), max.getBlockZ());
			BlockPos end = new BlockPos(min.getBlockX(), min.getBlockY(), min.getBlockZ());
			ConquestInstance instance = new ConquestInstance(worldName, text.getText(), start, end);
			try (Writer writer = new FileWriter(toSave)) {
				Gson gson = TaleOfKingdoms.getAPI().get().getMod().getGson();
				gson.toJson(instance, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}

			TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().addConquest(worldName, instance, true);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						int progress = Operations.getProgress(oi.getOperationId());
						int blocksDone = progress == 0 ? 0 : (oi.getBlocks() / (100 / progress));
						button.setMessage("Loading, please wait... (" + Operations.getProgress(oi.getOperationId()) + "%, " + blocksDone + "/" + oi.getBlocks() + ")");
						if (progress >= 100) {
							button.setMessage("Finishing pasting...");
							com.sk89q.worldedit.world.World adaptedWorld = ForgeAdapter.adapt(player.getEntityWorld());
							EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
							editSession.flushSession();

							Timer pastingTimer = new Timer();
							pastingTimer.schedule(new TimerTask() {
								@Override
								public void run() {
									button.setMessage("Finishing pasting... (" + progress + "%, " + blocksDone + "/" + oi.getBlocks() + ")");
									button.setMessage("Loading NPCs...");
									Minecraft.getInstance().runImmediately(() -> {
										try {
											BlockVector3 start = oi.getRegion().getMaximumPoint();
											BlockVector3 end = oi.getRegion().getMinimumPoint();
											int topBlockX = (Math.max(start.getBlockX(), end.getBlockX()));
											int bottomBlockX = (Math.min(start.getBlockX(), end.getBlockX()));

											int topBlockY = (Math.max(start.getBlockY(), end.getBlockY()));
											int bottomBlockY = (Math.min(start.getBlockY(), end.getBlockY()));

											int topBlockZ = (Math.max(start.getBlockZ(), end.getBlockZ()));
											int bottomBlockZ = (Math.min(start.getBlockZ(), end.getBlockZ()));

											for (int x = bottomBlockX; x <= topBlockX; x++) {
												for (int z = bottomBlockZ; z <= topBlockZ; z++) {
													for (int y = bottomBlockY; y <= topBlockY; y++) {
														BlockPos blockPos = new BlockPos(x, y, z);
														TileEntity tileEntity = player.getEntityWorld().getChunkAt(blockPos).getTileEntity(blockPos);
														if (tileEntity instanceof SignTileEntity) {
															SignTileEntity signTileEntity = (SignTileEntity) tileEntity;
															if (signTileEntity.getText(0).getFormattedText().equals("[Spawn]")) {
																String entityName = signTileEntity.getText(1).getFormattedText();
																button.setMessage("Loading NPCs: " + entityName);
																Class<? extends TOKEntity> entity = (Class<? extends TOKEntity>) Class.forName("net.islandearth.taleofkingdoms.common.entity.guild." + entityName + "Entity");
																Constructor constructor = entity.getConstructor(World.class);
																TOKEntity toSpawn = (TOKEntity) constructor.newInstance(player.getEntityWorld());
																toSpawn.setLocationAndAngles(x + 0.5, y, z + 0.5, 0, 0);
																player.getEntityWorld().addEntity(toSpawn);
																toSpawn.forceSetPosition(x + 0.5, y, z + 0.5);
																signTileEntity.getBlockState().getBlock().removedByPlayer(signTileEntity.getBlockState(), player.getEntityWorld(), blockPos, null, false, signTileEntity.getBlockState().getFluidState());
															}
														}
													}
												}
											}
										} catch (ReflectiveOperationException e) {
											button.setMessage("Error: " + e.getCause().getMessage());
											e.printStackTrace();
										}

										Minecraft.getInstance().runImmediately(() -> {
											button.setMessage("Reloading chunks...");
											minecraft.worldRenderer.loadRenderers();
											onClose();
											loading = false;
											instance.setLoaded(true);
											instance.setFarmerLastBread(-1); // Set to -1 in order to claim on first day
											pastingTimer.cancel();
										});
									});
								}
							}, 200);
							this.cancel();
						}
					} catch (Exception e) {
						button.setMessage("Error: " + e.getCause().getMessage());
						e.printStackTrace();
					}
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
}
