package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenBar;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.schematic.SchematicOptions;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class GuildMasterScreen extends ScreenTOK {

    private final Player player;
    private final GuildMasterEntity entity;
    private final ClientConquestInstance instance;
    private ScreenBar worthness;

    private Button signContractButton;
    private Button cancelContractButton;

    private Button hireHuntersButton;

    public GuildMasterScreen(Player player, GuildMasterEntity entity, ClientConquestInstance instance) {
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
            this.makeContractSignButton();
        } else {
            this.makeCancelContractButton();
        }

        this.makeHireHuntersButton();

        this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 2, 150, 20, new TextComponent("Retire Hunter"), (button) -> {
            if (instance.getHunterUUIDs().isEmpty()) {
                Translations.GUILDMASTER_NOHUNTER.send(player);
            } else {
                if (!instance.getHunterUUIDs().isEmpty()) {
                    Translations.HUNTER_THANK.send(player);
                    if (Minecraft.getInstance().getSingleplayerServer() == null) {
                        TaleOfKingdoms.getAPI().ifPresent(api -> {
                            api.getClientHandler(TaleOfKingdoms.HUNTER_PACKET_ID)
                                    .handleOutgoingPacket(TaleOfKingdoms.HUNTER_PACKET_ID,
                                            player,
                                            minecraft.getConnection().getConnection(),
                                            true);
                        });
                        this.onClose();
                        return;
                    }

                    ServerLevel serverWorld = Minecraft.getInstance().getSingleplayerServer().overworld();
                    HunterEntity hunter = (HunterEntity) serverWorld.getEntity(instance.getHunterUUIDs().get(0));
                    hunter.kill();
                    instance.removeHunter(hunter);
                    instance.setCoins(instance.getCoins() + 750);
                } else {
                    Translations.GUILDMASTER_NOHUNTER.send(player);
                }
            }
            this.onClose();
        }));

        Inventory clientPlayerInventory = player.getInventory();
        ItemStack stack = InventoryUtils.getStack(clientPlayerInventory, ItemTags.LOGS.getValues(), 64);
        String fixText = "Fix the guild";
        this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 2 + 23, 150, 20, new TextComponent(fixText), (button) -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> api.executeOnMain(() -> {
                if (instance.getCoins(player.getUUID()) < 3000) return;
                if (stack == null) return;
                if (Minecraft.getInstance().getSingleplayerServer() == null) {
                    api.getClientHandler(TaleOfKingdoms.FIX_GUILD_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.FIX_GUILD_PACKET_ID,
                                    player,
                                    minecraft.getConnection().getConnection());
                    return;
                }

                ServerPlayer serverPlayerEntity = Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayer(player.getUUID());
                if (serverPlayerEntity != null) {
                    Inventory serverPlayerInventory = serverPlayerEntity.getInventory();
                    serverPlayerInventory.setItem(serverPlayerInventory.findSlotMatchingItem(stack), new ItemStack(Items.AIR));
                    instance.setCoins(player.getUUID(), instance.getCoins(player.getUUID()) - 3000);
                    instance.rebuild(serverPlayerEntity, api, SchematicOptions.IGNORE_DEFENDERS);
                }
            }));
            this.onClose();
        }));

        this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 2 + 46, 150, 20, new TextComponent("Exit"), (button) -> {
            Translations.GUILDMASTER_GOODHUNTING.send(player);
            this.onClose();
        }));

        this.worthness = new ScreenBar(this.width / 2 - 65 , this.height / 2 + 83, 125, 12, 1.0F, ScreenBar.BarColour.RED);
        this.worthness.setBar(instance.getWorthiness() / 1500.0F);
    }

    @Override
    public void render(PoseStack stack, int par1, int par2, float par3) {
        super.render(stack, par1, par2, par3);
        String order = Translations.GUILDMASTER_GUILD_ORDER.getFormatted() + instance.getCoins() + " " + Translations.GOLD_COINS.getFormatted();
        String path = Translations.GUILDMASTER_PATH.getFormatted();
        drawCenteredString(stack, this.font, order, this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredString(stack, this.font, path, this.width / 2 , this.height / 2 + 70, 0XFFFFFF);
        drawCenteredString(stack, this.font, "Repairing the guild costs 64 logs and 3000 coins.", this.width / 2, this.height / 2 + 100, 0XFFFFFF);
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

    private void makeContractSignButton() {
        if (this.signContractButton != null) {
            signContractButton.visible = true;
            signContractButton.active = true;
            return;
        }

        this.signContractButton = this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 2 - 46, 150, 20, Translations.GUILDMASTER_CONTRACT_SIGN_UP.getTranslation(), (button) -> {
            if (Minecraft.getInstance().getSingleplayerServer() != null) {
                instance.setHasContract(true);
                Translations.GUILDMASTER_CONTRACT_SIGN.send(player);
            } else {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    api.getClientHandler(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID,
                                    player,
                                    minecraft.getConnection().getConnection(),
                                    true);
                });
            }
            button.visible = false;
            button.active = false;
            this.makeCancelContractButton();
        }));
    }

    private void makeCancelContractButton() {
        if (this.cancelContractButton != null) {
            cancelContractButton.visible = true;
            cancelContractButton.active = true;
            return;
        }

        this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 2 - 46, 150, 20, Translations.GUILDMASTER_CONTRACT_CANCEL.getTranslation(), (button) -> {
            if (Minecraft.getInstance().getSingleplayerServer() != null) {
                instance.setHasContract(false);
            } else {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    api.getClientHandler(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID,
                                    player,
                                    minecraft.getConnection().getConnection(),
                                    false);
                });
            }
            button.visible = false;
            button.active = false;
            this.makeContractSignButton();
        }));
    }

    private void makeHireHuntersButton() {
        String hunterText = instance.getCoins() >= 1500 ? "Hire Hunters " + ChatFormatting.GREEN + "(1500 gold)" : "Hire Hunters " + ChatFormatting.RED + "(1500 gold)";
        if (this.hireHuntersButton != null) {
            this.hireHuntersButton.setMessage(new TextComponent(hunterText));
            return;
        }

        this.hireHuntersButton = this.addRenderableWidget(new Button(this.width / 2 - 75, this.height / 2 - 23, 150, 20, new TextComponent(hunterText), (button) -> {
            if (instance.getCoins() >= 1500) {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    Translations.SERVE.send(player);
                    if (Minecraft.getInstance().getSingleplayerServer() == null) {
                        api.getClientHandler(TaleOfKingdoms.HUNTER_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.HUNTER_PACKET_ID,
                                        player,
                                        minecraft.getConnection().getConnection(),
                                        false);
                        return;
                    }

                    api.executeOnServer(() -> {
                        ServerLevel serverWorld = Minecraft.getInstance().getSingleplayerServer().overworld();
                        BlockPos blockPos = entity.blockPosition();
                        HunterEntity hunterEntity = EntityUtils.spawnEntity(EntityTypes.HUNTER, serverWorld, blockPos);
                        instance.addHunter(hunterEntity);
                        instance.setCoins(instance.getCoins() - 1500);
                    });
                });
                this.makeHireHuntersButton();
            }
        }));
    }
}
