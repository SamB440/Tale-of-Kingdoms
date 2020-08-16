package net.islandearth.taleofkingdoms.client.gui.generic;

import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.islandearth.taleofkingdoms.common.schematic.Schematic;
import net.islandearth.taleofkingdoms.common.schematic.SchematicHandler;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

import java.io.File;

public class ScreenStartConquest extends ScreenTOK {
	
	// Buttons
	private ButtonWidget mButtonClose;
	
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
		this.text = new TextFieldWidget(this.textRenderer, this.width / 2 - 150, this.height / 2 - 40, 300, 20, new LiteralText("Sir Punchwood"));
		this.addButton(mButtonClose = new ButtonWidget(this.width / 2 - 100, this.height / 2 + 30, 200, 20, new LiteralText("Start your Conquest."), (button) -> {
			if (loading) return;

			button.setMessage(new LiteralText("Loading, please wait..."));
			// Load guild castle schematic
			SchematicHandler.pasteSchematic(Schematic.GUILD_CASTLE, player).thenAccept(oi -> {
				/*MinecraftClient.getInstance().execute(() -> {
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
					
					try {
						TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().addConquest(worldName, instance, true);
						button.setMessage(new LiteralText("Loading NPCs..."));
						int topBlockX = (Math.max(max.getBlockX(), min.getBlockX()));
						int bottomBlockX = (Math.min(max.getBlockX(), min.getBlockX()));
						
						int topBlockY = (Math.max(max.getBlockY(), min.getBlockY()));
						int bottomBlockY = (Math.min(max.getBlockY(), min.getBlockY()));
						
						int topBlockZ = (Math.max(max.getBlockZ(), min.getBlockZ()));
						int bottomBlockZ = (Math.min(max.getBlockZ(), min.getBlockZ()));
						
						for (int x = bottomBlockX; x <= topBlockX; x++) {
							for (int z = bottomBlockZ; z <= topBlockZ; z++) {
								for (int y = bottomBlockY; y <= topBlockY; y++) {
									BlockPos blockPos = new BlockPos(x, y, z);
									BlockEntity tileEntity = player.getEntityWorld().getChunk(blockPos).getBlockEntity(blockPos);
									if (tileEntity instanceof SignBlockEntity) {
										SignBlockEntity signTileEntity = (SignBlockEntity) tileEntity;
										Tag line1 = signTileEntity.toInitialChunkDataTag().get("Text1");
										if (line1 != null && line1.asString().equals("[Spawn]")) {
											Tag line2 = signTileEntity.toInitialChunkDataTag().get("Text1");
											String entityName = line2.asString();
											button.setMessage(new LiteralText("Loading NPCs: " + entityName));
											Class<? extends TOKEntity> entity = (Class<? extends TOKEntity>) Class.forName("net.islandearth.taleofkingdoms.common.entity.guild." + entityName + "Entity");
											Constructor constructor = entity.getConstructor(World.class);
											TOKEntity toSpawn = (TOKEntity) constructor.newInstance(player.getEntityWorld());
											player.getEntityWorld().spawnEntity(toSpawn);
											toSpawn.teleport(x + 0.5, y, z + 0.5);
											signTileEntity.getCachedState().getBlock().onBreak(player.getEntityWorld(), blockPos, signTileEntity.getCachedState(), null);
											System.out.println("Spawned entity " + entityName + " " + toSpawn.toString() + " " + toSpawn.getX() + "," + toSpawn.getY() + "," + toSpawn.getZ());
										}
									}
								}
							}
						}
						button.setMessage(new LiteralText("Reloading chunks..."));
						MinecraftClient.getInstance().worldRenderer.reload();
						onClose();
						loading = false;
						instance.setLoaded(true);
						instance.setFarmerLastBread(-1); // Set to -1 in order to claim on first day
					} catch (ReflectiveOperationException e) {
						e.printStackTrace();
					}
				});*/
			});
		}));
		this.text.setMaxLength(32);
		this.text.setText("Sir Punchwood");
		this.text.setFocusUnlocked(false);
	    this.text.changeFocus(true);
		this.text.setVisible(true);
		this.children.add(this.text);
	}
	
	@Override
	public void render(MatrixStack stack, int par1, int par2, float par3) {
        this.renderBackground(stack);
        this.drawCenteredString(stack, this.textRenderer, "The Great Tides of Darkness are coming. Build your forces and vanquish evil.", this.width / 2, this.height / 2, 0xFFFFFF);
        this.drawCenteredString(stack, this.textRenderer, "Be the hero you were born for. The Guild will prepare you...", this.width / 2, this.height / 2 + 10, 0xFFFFFF);
        this.text.render(stack, par1, par2, par3);
        super.render(stack, par1, par2, par3);
    }
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
