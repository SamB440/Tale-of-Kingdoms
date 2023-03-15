package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenBar;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.schematic.SchematicOptions;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.UUID;


public class GuildMasterScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final GuildMasterEntity entity;
    private final ConquestInstance instance;
    private ScreenBar worthness;

    private ButtonWidget signContractButton;
    private ButtonWidget cancelContractButton;

    private ButtonWidget hireHuntersButton;

    public GuildMasterScreen(PlayerEntity player, GuildMasterEntity entity, ConquestInstance instance) {
        super("taleofkingdoms.menu.guildmaster.name");
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.GUILDMASTER_WELCOME.send(player);
    }

    @Override
    public void init() {
        final UUID playerUuid = player.getUuid();
        super.init();
        if (!instance.hasContract(playerUuid)) {
            this.makeContractSignButton();
        } else {
            this.makeCancelContractButton();
        }

        this.makeHireHuntersButton();

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Retire Hunter"), widget -> {
            if (instance.getHunterUUIDs().isEmpty()) {
                Translations.GUILDMASTER_NOHUNTER.send(player);
            } else {
                if (!instance.getHunterUUIDs().isEmpty()) {
                    Translations.HUNTER_THANK.send(player);
                    if (MinecraftClient.getInstance().getServer() == null) {
                        TaleOfKingdoms.getAPI().getClientHandler(TaleOfKingdoms.HUNTER_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.HUNTER_PACKET_ID,
                                        player,
                                        client.getNetworkHandler().getConnection(),
                                        true);
                        this.close();
                        return;
                    }

                    ServerWorld serverWorld = MinecraftClient.getInstance().getServer().getOverworld();
                    for (UUID uuid : instance.getHunterUUIDs().getOrDefault(playerUuid, new ArrayList<>())) {
                        HunterEntity hunter = (HunterEntity) serverWorld.getEntity(uuid);
                        if (hunter == null) {
                            instance.removeHunter(playerUuid, uuid);
                            TaleOfKingdoms.LOGGER.info("Removed hunter by uuid " + uuid + " that no longer exists.");
                        } else {
                            TaleOfKingdoms.getAPI().executeOnServer(hunter::kill);
                            instance.removeHunter(playerUuid, hunter.getUuid());
                            instance.setCoins(playerUuid, instance.getCoins(playerUuid) + 750);
                            return;
                        }
                    }
                    player.sendMessage(Text.literal("Unable to find an alive hunter!"), false);
                    return;
                } else {
                    Translations.GUILDMASTER_NOHUNTER.send(player);
                }
            }
            this.close();
        }).dimensions(this.width / 2 - 75, this.height / 2, 150, 20).build());

        PlayerInventory clientPlayerInventory = player.getInventory();
        ItemStack stack = InventoryUtils.getStack(clientPlayerInventory, ItemTags.LOGS, 64);
        String fixText = "Fix the guild";
        this.addDrawableChild(ButtonWidget.builder(Text.literal(fixText), widget -> {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            api.executeOnMain(() -> {
                if (instance.getCoins(playerUuid) < 3000) return;
                if (stack == null) return;
                if (MinecraftClient.getInstance().getServer() == null) {
                    api.getClientHandler(TaleOfKingdoms.FIX_GUILD_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.FIX_GUILD_PACKET_ID,
                                    player,
                                    client.getNetworkHandler().getConnection());
                    return;
                }

                ServerPlayerEntity serverPlayerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(playerUuid);
                if (serverPlayerEntity != null) {
                    PlayerInventory serverPlayerInventory = serverPlayerEntity.getInventory();
                    serverPlayerInventory.setStack(serverPlayerInventory.getSlotWithStack(stack), new ItemStack(Items.AIR));
                    instance.setCoins(playerUuid, instance.getCoins(playerUuid) - 3000);
                    instance.rebuild(serverPlayerEntity, api, SchematicOptions.IGNORE_DEFENDERS);
                }
            });
            this.close();
        }).dimensions(this.width / 2 - 75, this.height / 2 + 23, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Exit"), widget -> {
            Translations.GUILDMASTER_GOODHUNTING.send(player);
            this.close();
        }).dimensions(this.width / 2 - 75, this.height / 2 + 46, 150, 20).build());

        this.worthness = new ScreenBar(this.width / 2 - 65 , this.height / 2 + 83, 125, 12, 1.0F, ScreenBar.BarColour.RED);
        this.worthness.setBar(instance.getWorthiness(playerUuid) / 1500.0F);
    }

    @Override
    public void render(MatrixStack stack, int par1, int par2, float par3) {
        super.render(stack, par1, par2, par3);
        String order = Translations.GUILDMASTER_GUILD_ORDER.getFormatted() + instance.getCoins(player.getUuid()) + " " + Translations.GOLD_COINS.getFormatted();
        String path = Translations.GUILDMASTER_PATH.getFormatted();
        drawCenteredTextWithShadow(stack, this.textRenderer, order, this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredTextWithShadow(stack, this.textRenderer, path, this.width / 2 , this.height / 2 + 70, 0XFFFFFF);
        drawCenteredTextWithShadow(stack, this.textRenderer, "Repairing the guild costs 64 logs and 3000 coins.", this.width / 2, this.height / 2 + 100, 0XFFFFFF);
        this.worthness.drawBar(stack);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private void makeContractSignButton() {
        if (this.signContractButton != null) {
            signContractButton.visible = true;
            signContractButton.active = true;
            return;
        }

        this.signContractButton = this.addDrawableChild(ButtonWidget.builder(Translations.GUILDMASTER_CONTRACT_SIGN_UP.getTranslation(), widget -> {
            if (MinecraftClient.getInstance().getServer() != null) {
                instance.setHasContract(player.getUuid(), true);
                Translations.GUILDMASTER_CONTRACT_SIGN.send(player);
            } else {
                TaleOfKingdoms.getAPI().getClientHandler(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID)
                        .handleOutgoingPacket(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID,
                                player, true);
            }
            widget.visible = false;
            widget.active = false;
            this.makeCancelContractButton();
        }).dimensions(this.width / 2 - 75, this.height / 2 - 46, 150, 20).build());
    }

    private void makeCancelContractButton() {
        if (this.cancelContractButton != null) {
            cancelContractButton.visible = true;
            cancelContractButton.active = true;
            return;
        }

        this.addDrawableChild(ButtonWidget.builder(Translations.GUILDMASTER_CONTRACT_CANCEL.getTranslation(), widget -> {
            if (MinecraftClient.getInstance().getServer() != null) {
                instance.setHasContract(player.getUuid(),false);
                Translations.GUILDMASTER_CONTRACT_CANCEL_AWAIT.send(player);
            } else {
                TaleOfKingdoms.getAPI().getClientHandler(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID)
                        .handleOutgoingPacket(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID,
                                player, false);
            }
            widget.visible = false;
            widget.active = false;
            this.makeContractSignButton();
        }).dimensions(this.width / 2 - 75, this.height / 2 - 46, 150, 20).build());
    }

    private void makeHireHuntersButton() {
        final UUID playerUuid = player.getUuid();
        String hunterText = instance.getCoins(playerUuid) >= 1500 ? "Hire Hunters " + Formatting.GREEN + "(1500 gold)" : "Hire Hunters " + Formatting.RED + "(1500 gold)";
        if (this.hireHuntersButton != null) {
            this.hireHuntersButton.setMessage(Text.literal(hunterText));
            return;
        }

        this.hireHuntersButton = this.addDrawableChild(ButtonWidget.builder(Text.literal(hunterText), widget -> {
            if (instance.getCoins(playerUuid) >= 1500) {
                final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
                Translations.SERVE.send(player);
                if (MinecraftClient.getInstance().getServer() == null) {
                    api.getClientHandler(TaleOfKingdoms.HUNTER_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.HUNTER_PACKET_ID,
                                    player, false);
                    return;
                }

                api.executeOnServer(() -> {
                    ServerWorld serverWorld = MinecraftClient.getInstance().getServer().getOverworld();
                    BlockPos blockPos = entity.getBlockPos();
                    HunterEntity hunterEntity = EntityUtils.spawnEntity(EntityTypes.HUNTER, serverWorld, blockPos);
                    instance.addHunter(playerUuid, hunterEntity);
                    instance.setCoins(playerUuid, instance.getCoins(playerUuid) - 1500);
                });

                this.makeHireHuntersButton();
            }
        }).dimensions(this.width / 2 - 75, this.height / 2 - 23, 150, 20).build());
    }
}
