package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.event.tok.KingdomStartCallback;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.google.gson.Gson;
import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;

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
        this.addButton(mButtonClose = new ButtonWidget(this.width / 2 - 100, this.height / 2 + 30, 200, 20, Translations.START_CONQUEST.getTranslation(), (button) -> {
            if (loading) return;

            button.setMessage(Translations.BUILDING_CASTLE.getTranslation());
            if (!TaleOfKingdoms.getAPI().isPresent()) {
                button.setMessage(new LiteralText("No API present"));
                return;
            }

            TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
            MinecraftServer server = MinecraftClient.getInstance().getServer();
            if (server == null) {
                button.setMessage(new LiteralText("No server present"));
                return;
            }
            ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
            if (serverPlayer == null) return;

            // Load guild castle schematic
            api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, serverPlayer).thenAccept(oi -> {
                api.executeOnServer(() -> {
                    BlockVector3 max = oi.getRegion().getMaximumPoint();
                    BlockVector3 min = oi.getRegion().getMinimumPoint();
                    BlockPos start = new BlockPos(max.getBlockX(), max.getBlockY(), max.getBlockZ());
                    BlockPos end = new BlockPos(min.getBlockX(), min.getBlockY(), min.getBlockZ());
                    ClientConquestInstance instance = new ClientConquestInstance(worldName, text.getText(), start, end, serverPlayer.getBlockPos().add(0, 1, 0));
                    try (Writer writer = new FileWriter(toSave)) {
                        Gson gson = TaleOfKingdoms.getAPI().get().getMod().getGson();
                        gson.toJson(instance, writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().addConquest(worldName, instance, true);
                        button.setMessage(Translations.SUMMONING_CITIZENS.getTranslation());
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
                                    BlockEntity tileEntity = serverPlayer.getServerWorld().getChunk(blockPos).getBlockEntity(blockPos);
                                    if (tileEntity instanceof SignBlockEntity) {
                                        SignBlockEntity signTileEntity = (SignBlockEntity) tileEntity;
                                        Tag line1 = signTileEntity.toInitialChunkDataTag().get("Text1");
                                        if (line1 != null && line1.toText().getString().equals("'{\"text\":\"[Spawn]\"}'")) {
                                            Tag line2 = signTileEntity.toInitialChunkDataTag().get("Text2");
                                            String entityName = line2.toText().getString().replace("'{\"text\":\"", "").replace("\"}'", ""); // Doesn't seem to be a way to get the plain string...
                                            button.setMessage(new LiteralText(Translations.NEW_CITIZEN.getFormatted() + entityName));
                                            Class<? extends TOKEntity> entity = (Class<? extends TOKEntity>) Class.forName("com.convallyria.taleofkingdoms.common.entity.guild." + entityName + "Entity");
                                            Constructor constructor = entity.getConstructor(EntityType.class, World.class);
                                            EntityType type = (EntityType) EntityTypes.class.getField(entityName.toUpperCase()).get(EntityTypes.class);
                                            TOKEntity toSpawn = (TOKEntity) constructor.newInstance(type, serverPlayer.getServer().getOverworld());
                                            toSpawn.setPos(x + 0.5, y, z + 0.5);
                                            serverPlayer.getServer().getOverworld().spawnEntity(toSpawn);
                                            serverPlayer.getServer().getOverworld().breakBlock(blockPos, false);
                                            toSpawn.refreshPositionAfterTeleport(x + 0.5, y, z + 0.5);
                                            TaleOfKingdoms.LOGGER.info("Spawned entity " + entityName + " " + toSpawn.toString() + " " + toSpawn.getX() + "," + toSpawn.getY() + "," + toSpawn.getZ());
                                        }
                                    }
                                }
                            }
                        }

                        api.executeOnMain(() -> {
                            button.setMessage(new LiteralText("Reloading chunks..."));
                            MinecraftClient.getInstance().worldRenderer.reload();
                            onClose();
                            loading = false;
                            instance.setLoaded(true);
                            instance.setFarmerLastBread(-1); // Set to -1 in order to claim on first day
                        });

                        KingdomStartCallback.EVENT.invoker().kingdomStart(serverPlayer, instance); // Call kingdom start event
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                });
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
        drawCenteredString(stack, this.textRenderer, Translations.DARKNESS.getFormatted(), this.width / 2, this.height / 2, 0xFFFFFF);
        drawCenteredString(stack, this.textRenderer, Translations.HERO.getFormatted(), this.width / 2, this.height / 2 + 10, 0xFFFFFF);
        this.text.render(stack, par1, par2, par3);
        super.render(stack, par1, par2, par3);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
