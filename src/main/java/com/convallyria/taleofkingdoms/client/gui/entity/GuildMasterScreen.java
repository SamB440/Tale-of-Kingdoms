package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenBar;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.google.common.collect.ImmutableList;
import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class GuildMasterScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final GuildMasterEntity entity;
    private final ClientConquestInstance instance;
    private ScreenBar worthness;
    private final ImmutableList<Item> logs = ImmutableList.of(Items.ACACIA_LOG,
            Items.BIRCH_LOG,
            Items.DARK_OAK_LOG,
            Items.JUNGLE_LOG,
            Items.OAK_LOG,
            Items.SPRUCE_LOG);

    public GuildMasterScreen(PlayerEntity player, GuildMasterEntity entity, ClientConquestInstance instance) {
        super("taleofkingdoms.menu.guildmaster.name");
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.GUILDMASTER_WELCOME.send(player);
    }

    @Override
    public void init() {
        super.init();
        if (!instance.hasContract()) {
            this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 46, 150, 20, Translations.GUILDMASTER_CONTRACT_SIGN_UP.getTranslation(), (button) -> {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    api.getClientHandler(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID,
                                    player,
                                    client.getNetworkHandler().getConnection(),
                                    true);
                });
                button.visible = false;
                button.active = false;
                this.onClose();
                MinecraftClient.getInstance().openScreen(new GuildMasterScreen(player, entity, instance));
            }));
        } else {
            this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 46, 150, 20, Translations.GUILDMASTER_CONTRACT_CANCEL.getTranslation(), (button) -> {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    api.getClientHandler(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID,
                                    player,
                                    client.getNetworkHandler().getConnection(),
                                    false);
                });
                button.visible = false;
                button.active = false;
            }));
        }

        String hunterText = instance.getCoins() >= 1500 ? "Hire Hunters " + Formatting.GREEN + "(1500 gold)" : "Hire Hunters " + Formatting.RED + "(1500 gold)";
        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 23, 150, 20, new LiteralText(hunterText), (button) -> {
            if (instance.getCoins() >= 1500) {
                TaleOfKingdoms.getAPI().ifPresent(api -> api.executeOnServer(() -> {
                    if (MinecraftClient.getInstance().getServer() != null) {
                        ServerWorld serverWorld = MinecraftClient.getInstance().getServer().getOverworld();
                        HunterEntity hunterEntity = new HunterEntity(EntityTypes.HUNTER, serverWorld);
                        hunterEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
                        serverWorld.spawnEntity(hunterEntity);
                        hunterEntity.teleport(entity.getX(), entity.getY(), entity.getZ());
                    }
                }));
                instance.setCoins(instance.getCoins() - 1500);
                Translations.SERVE.send(player);
                this.onClose();
                MinecraftClient.getInstance().openScreen(new GuildMasterScreen(player, entity, instance));
            }
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 2, 150, 20, new LiteralText("Fix the guild"), (button) -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                api.executeOnMain(() -> {
                    ServerPlayerEntity serverPlayerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                    if (serverPlayerEntity != null) {
                        PlayerInventory playerInventory = serverPlayerEntity.inventory;
                        ItemStack stack = null;
                        for (Item log : logs) {
                            ItemStack logStack = new ItemStack(log);
                            if (playerInventory.contains(logStack)) {
                                stack = logStack;
                            }
                        }

                        if (stack != null) {
                            playerInventory.setStack(playerInventory.getSlotWithStack(stack), new ItemStack(Items.AIR));
                            BlockPos origin = instance.getOrigin();
                            BlockVector3 blockVector3 = BlockVector3.at(origin.getX(), origin.getY(), origin.getZ());
                            api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, serverPlayerEntity, blockVector3).thenAccept(oi -> {
                                api.executeOnServer(() -> {
                                    BlockVector3 max = oi.getRegion().getMaximumPoint();
                                    BlockVector3 min = oi.getRegion().getMinimumPoint();

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
                                                BlockEntity tileEntity = serverPlayerEntity.getServerWorld().getChunk(blockPos).getBlockEntity(blockPos);
                                                if (tileEntity instanceof SignBlockEntity) {
                                                    SignBlockEntity signTileEntity = (SignBlockEntity) tileEntity;
                                                    Tag line1 = signTileEntity.toInitialChunkDataTag().get("Text1");
                                                    if (line1 != null && line1.toText().getString().equals("'{\"text\":\"[Spawn]\"}'")) {
                                                        serverPlayerEntity.getServerWorld().breakBlock(blockPos, false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                            });
                        }
                    }
                });
            });

            this.onClose();
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 2 + 23, 150, 20, new LiteralText("Exit"), (button) -> {
            Translations.GUILDMASTER_GOODHUNTING.send(player);
            this.onClose();
        }));

        this.worthness = new ScreenBar(this.width / 2 - 65, this.height / 2 + 65, 125, 12, 1.0F, ScreenBar.BarColour.RED);
        this.worthness.setBar(instance.getWorthiness() / 10000.0F);
    }

    @Override
    public void render(MatrixStack stack, int par1, int par2, float par3) {
        super.render(stack, par1, par2, par3);
        String order = Translations.GUILDMASTER_GUILD_ORDER.getFormatted() + instance.getCoins() + " " + Translations.GOLD_COINS.getFormatted();
        String path = Translations.GUILDMASTER_PATH.getFormatted();
        drawCenteredString(stack, this.textRenderer, order, this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredString(stack, this.textRenderer, path, this.width / 2, this.height / 2 + 50, 0XFFFFFF);
        this.worthness.drawBar(stack);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
