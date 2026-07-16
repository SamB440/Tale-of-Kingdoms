package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClientAPI;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.generic.bar.BarColour;
import com.convallyria.taleofkingdoms.client.gui.generic.bar.ScreenBar;
import com.convallyria.taleofkingdoms.common.packet.c2s.ChangeTitlePacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.SignContractPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.FixGuildPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.HireHunterPacket;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.schematic.SchematicOptions;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.common.world.guild.PlayerTitle;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class GuildMasterScreen extends ScreenTOK {

    private final Player player;
    private final GuildMasterEntity entity;
    private final ConquestInstance instance;
    private final GuildPlayer guildPlayer;
    private ScreenBar worthness;

    private Button signContractButton;
    private Button cancelContractButton;

    private Button hireHuntersButton;

    public GuildMasterScreen(Player player, GuildMasterEntity entity, ConquestInstance instance) {
        super("menu.taleofkingdoms.guild_master.name");
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        this.guildPlayer = instance.getPlayer(player.getUUID());
        Translations.GUILDMASTER_WELCOME.send(player);
    }

    @Override
    public void init() {
        super.init();

        if (!guildPlayer.hasSignedContract()) {
            this.makeContractSignButton();
        } else {
            this.makeCancelContractButton();
        }

        this.makeHireHuntersButton();
        this.makeTitleButton();

        this.addRenderableWidget(Button.builder(Component.translatable("menu.taleofkingdoms.guild_master.retire_hunter"), widget -> {
            if (guildPlayer.getHunters().isEmpty()) {
                Translations.GUILDMASTER_NOHUNTER.send(player);
            } else {
                if (!guildPlayer.getHunters().isEmpty()) {
                    Translations.HUNTER_THANK.send(player, PlayerTitle.of(player).getTranslation());
                    if (Minecraft.getInstance().getSingleplayerServer() == null) {
                        TaleOfKingdomsClient.getAPI().getClientPacket(Packets.HIRE_HUNTER)
                                .sendPacket(player, new HireHunterPacket(true));
                        this.onClose();
                        return;
                    }

                    ServerLevel serverWorld = Minecraft.getInstance().getSingleplayerServer().overworld();
                    final List<UUID> list = List.copyOf(guildPlayer.getHunters());
                    for (UUID uuid : list) {
                        HunterEntity hunter = (HunterEntity) serverWorld.getEntity(uuid);
                        if (hunter == null) {
                            guildPlayer.getHunters().remove(uuid);
                            TaleOfKingdoms.LOGGER.info("Removed hunter by uuid " + uuid + " that no longer exists.");
                        } else {
                            TaleOfKingdoms.getAPI().executeOnServerEnvironment((s) -> hunter.remove(Entity.RemovalReason.DISCARDED));
                            guildPlayer.getHunters().remove(hunter.getUUID());
                            guildPlayer.setCoins(guildPlayer.getCoins() + 750);
                            return;
                        }
                    }
                    guildPlayer.getHunters().clear();
                    guildPlayer.getHunters().addAll(list);
                    player.displayClientMessage(Component.literal("Unable to find an alive hunter!"), false);
                    return;
                } else {
                    Translations.GUILDMASTER_NOHUNTER.send(player);
                }
            }
            this.onClose();
        }).bounds(this.width / 2 - 75, this.height / 2, 150, 20).build());

        Inventory clientPlayerInventory = player.getInventory();
        ItemStack stack = InventoryUtils.getStack(clientPlayerInventory, ItemTags.LOGS, 64);
        final Button fixWidget = this.addRenderableWidget(Button.builder(Component.translatable("menu.taleofkingdoms.guild_master.fix_guild"), widget -> {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            api.executeOnMain(() -> {
                if (stack == null || guildPlayer.getCoins() < 3000) {
                    Translations.GUILDMASTER_NOT_ENOUGH_RESOURCES.send(player);
                    return;
                }

                if (Minecraft.getInstance().getSingleplayerServer() == null) {
                    ((TaleOfKingdomsClientAPI) api).getClientPacket(Packets.FIX_GUILD)
                            .sendPacket(player, new FixGuildPacket());
                    return;
                }

                ServerPlayer serverPlayerEntity = Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayer(player.getUUID());
                if (serverPlayerEntity != null) {
                    Inventory serverPlayerInventory = serverPlayerEntity.getInventory();
                    serverPlayerInventory.setItem(serverPlayerInventory.findSlotMatchingItem(stack), new ItemStack(Items.AIR));
                    guildPlayer.setCoins(guildPlayer.getCoins() - 3000);
                    instance.rebuild(serverPlayerEntity, api, SchematicOptions.IGNORE_DEFENDERS);
                }
            });
            this.onClose();
        }).bounds(this.width / 2 - 75, this.height / 2 + 23, 150, 20).build());
        fixWidget.active = !instance.isUnderAttack();
        if (instance.isUnderAttack()) {
            fixWidget.tooltip(List.of(
                    Component.literal("The Guild is under attack!"),
                    Component.literal("You must kill all reficules and speak to the Guild Master Defender."))
            );
        }

        this.addRenderableWidget(Button.builder(Component.translatable("menu.taleofkingdoms.generic.exit"), widget -> {
            Translations.GUILDMASTER_GOODHUNTING.send(player);
            this.onClose();
        }).bounds(this.width / 2 - 75, this.height / 2 + 46, 150, 20).build());

        this.worthness = new ScreenBar(this.width / 2 - 65 , this.height / 2 + 83, 125, 12, 1.0F, BarColour.RED);
        this.worthness.setBar(guildPlayer.getWorthiness() / 1500.0F);
    }

    @Override
    public void render(GuiGraphics context, int par1, int par2, float par3) {
        super.render(context, par1, par2, par3);
        String order = Translations.GUILDMASTER_GUILD_ORDER.getFormatted() + guildPlayer.getCoins() + " " + Translations.GOLD_COINS.getFormatted();
        String path = Translations.GUILDMASTER_PATH.getFormatted();
        context.drawCenteredString(this.font, order, this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        context.drawCenteredString(this.font, path, this.width / 2 , this.height / 2 + 70, 0XFFFFFF);
        context.drawCenteredString(this.font, "Repairing the guild costs 64 logs and 3000 coins.", this.width / 2, this.height / 2 + 100, 0XFFFFFF);
        this.worthness.drawBar(context);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    private void makeContractSignButton() {
        if (this.signContractButton != null) {
            signContractButton.visible = true;
            signContractButton.active = true;
            return;
        }

        this.signContractButton = this.addRenderableWidget(Button.builder(Translations.GUILDMASTER_CONTRACT_SIGN_UP.getTranslation(), widget -> {
            if (Minecraft.getInstance().getSingleplayerServer() != null) {
                guildPlayer.setSignedContract(true);
                Translations.GUILDMASTER_CONTRACT_SIGN.send(player);
            } else {
                TaleOfKingdomsClient.getAPI().getClientPacket(Packets.SIGN_CONTRACT)
                        .sendPacket(player, new SignContractPacket(true));
            }
            widget.visible = false;
            widget.active = false;
            this.makeCancelContractButton();
        }).bounds(this.width / 2 - 75, this.height / 2 - 46, 150, 20).build());
    }

    private void makeCancelContractButton() {
        if (this.cancelContractButton != null) {
            cancelContractButton.visible = true;
            cancelContractButton.active = true;
            return;
        }

        this.addRenderableWidget(Button.builder(Translations.GUILDMASTER_CONTRACT_CANCEL.getTranslation(), widget -> {
            if (Minecraft.getInstance().getSingleplayerServer() != null) {
                guildPlayer.setSignedContract(false);
                Translations.GUILDMASTER_CONTRACT_CANCEL_AWAIT.send(player);
            } else {
                TaleOfKingdomsClient.getAPI().getClientPacket(Packets.SIGN_CONTRACT)
                        .sendPacket(player, new SignContractPacket(false));
            }
            widget.visible = false;
            widget.active = false;
            this.makeContractSignButton();
        }).bounds(this.width / 2 - 75, this.height / 2 - 46, 150, 20).build());
    }

    private void makeTitleButton() {
        this.addRenderableWidget(Button.builder(getTitleButtonText(), widget -> {
            final PlayerTitle[] titles = PlayerTitle.values();
            final PlayerTitle next = titles[(guildPlayer.getTitle().ordinal() + 1) % titles.length];
            guildPlayer.setTitle(next);
            if (Minecraft.getInstance().getSingleplayerServer() == null) {
                TaleOfKingdomsClient.getAPI().getClientPacket(Packets.CHANGE_TITLE)
                        .sendPacket(player, new ChangeTitlePacket(next));
            }
            widget.setMessage(getTitleButtonText());
        }).bounds(this.width / 2 - 75, this.height / 2 - 69, 150, 20).build());
    }

    private Component getTitleButtonText() {
        return Component.translatable("menu.taleofkingdoms.guild_master.title", guildPlayer.getTitle().getTranslation());
    }

    private void makeHireHuntersButton() {
        MutableComponent hunterText = Component.translatable("menu.taleofkingdoms.guild_master.hire_hunter").withStyle(guildPlayer.getCoins() >= 1500 ? ChatFormatting.GREEN : ChatFormatting.RED);
        if (this.hireHuntersButton != null) {
            this.hireHuntersButton.setMessage(hunterText);
            return;
        }

        this.hireHuntersButton = this.addRenderableWidget(Button.builder(hunterText, widget -> {
            if (guildPlayer.getCoins() >= 1500) {
                final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
                Translations.HUNTER_SERVE.send(player, PlayerTitle.of(player).getTranslation());
                if (Minecraft.getInstance().getSingleplayerServer() == null) {
                    api.getClientPacket(Packets.HIRE_HUNTER)
                            .sendPacket(player, new HireHunterPacket(false));
                    return;
                }

                api.executeOnServerEnvironment((server) -> {
                    ServerLevel serverWorld = server.overworld();
                    BlockPos blockPos = entity.blockPosition();
                    HunterEntity hunterEntity = EntityUtils.spawnEntity(EntityTypes.HUNTER.get(), serverWorld, blockPos);
                    guildPlayer.getHunters().add(hunterEntity.getUUID());
                    guildPlayer.setCoins(guildPlayer.getCoins() - 1500);
                });

                this.makeHireHuntersButton();
            }
        }).bounds(this.width / 2 - 75, this.height / 2 - 23, 150, 20).build());
    }
}
