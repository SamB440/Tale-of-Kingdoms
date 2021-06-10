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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.ItemTags;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;


public class GuildMasterScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final GuildMasterEntity entity;
    private final ClientConquestInstance instance;
    private ScreenBar worthness;

    private ButtonWidget signContractButton;
    private ButtonWidget cancelContractButton;

    private ButtonWidget hireHuntersButton;

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
            this.makeContractSignButton();
        } else {
            this.makeCancelContractButton();
        }

        this.makeHireHuntersButton();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 2, 150, 20, new LiteralText("Retire Hunter"), (button) -> {
            if (instance.getHunterUUIDs().isEmpty()) {
                Translations.GUILDMASTER_NOHUNTER.send(player);
            } else {
                if (!instance.getHunterUUIDs().isEmpty()) {
                    Translations.HUNTER_THANK.send(player);
                    if (MinecraftClient.getInstance().getServer() == null) {
                        TaleOfKingdoms.getAPI().ifPresent(api -> {
                            api.getClientHandler(TaleOfKingdoms.HUNTER_PACKET_ID)
                                    .handleOutgoingPacket(TaleOfKingdoms.HUNTER_PACKET_ID,
                                            player,
                                            client.getNetworkHandler().getConnection(),
                                            true);
                        });
                        this.onClose();
                        return;
                    }

                    ServerWorld serverWorld = MinecraftClient.getInstance().getServer().getOverworld();
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

        PlayerInventory clientPlayerInventory = player.getInventory();
        ItemStack stack = InventoryUtils.getStack(clientPlayerInventory, ItemTags.LOGS.values(), 64);
        String fixText = "Fix the guild";
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 2 + 23, 150, 20, new LiteralText(fixText), (button) -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> api.executeOnMain(() -> {
                if (instance.getCoins(player.getUuid()) < 3000) return;
                if (stack == null) return;
                if (MinecraftClient.getInstance().getServer() == null) {
                    api.getClientHandler(TaleOfKingdoms.FIX_GUILD_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.FIX_GUILD_PACKET_ID,
                                    player,
                                    client.getNetworkHandler().getConnection());
                    return;
                }

                ServerPlayerEntity serverPlayerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                if (serverPlayerEntity != null) {
                    PlayerInventory serverPlayerInventory = serverPlayerEntity.getInventory();
                    serverPlayerInventory.setStack(serverPlayerInventory.getSlotWithStack(stack), new ItemStack(Items.AIR));
                    instance.setCoins(player.getUuid(), instance.getCoins(player.getUuid()) - 3000);
                    instance.rebuild(serverPlayerEntity, api, SchematicOptions.IGNORE_DEFENDERS);
                }
            }));
            this.onClose();
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 2 + 46, 150, 20, new LiteralText("Exit"), (button) -> {
            Translations.GUILDMASTER_GOODHUNTING.send(player);
            this.onClose();
        }));

        this.worthness = new ScreenBar(this.width / 2 - 65 , this.height / 2 + 83, 125, 12, 1.0F, ScreenBar.BarColour.RED);
        this.worthness.setBar(instance.getWorthiness() / 1500.0F);
    }

    @Override
    public void render(MatrixStack stack, int par1, int par2, float par3) {
        super.render(stack, par1, par2, par3);
        String order = Translations.GUILDMASTER_GUILD_ORDER.getFormatted() + instance.getCoins() + " " + Translations.GOLD_COINS.getFormatted();
        String path = Translations.GUILDMASTER_PATH.getFormatted();
        drawCenteredText(stack, this.textRenderer, order, this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredText(stack, this.textRenderer, path, this.width / 2 , this.height / 2 + 70, 0XFFFFFF);
        drawCenteredText(stack, this.textRenderer, "Repairing the guild costs 64 logs and 3000 coins.", this.width / 2, this.height / 2 + 100, 0XFFFFFF);
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

        this.signContractButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 46, 150, 20, Translations.GUILDMASTER_CONTRACT_SIGN_UP.getTranslation(), (button) -> {
            if (MinecraftClient.getInstance().getServer() != null) {
                instance.setHasContract(true);
                Translations.GUILDMASTER_CONTRACT_SIGN.send(player);
            } else {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    api.getClientHandler(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID,
                                    player,
                                    client.getNetworkHandler().getConnection(),
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

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 46, 150, 20, Translations.GUILDMASTER_CONTRACT_CANCEL.getTranslation(), (button) -> {
            if (MinecraftClient.getInstance().getServer() != null) {
                instance.setHasContract(false);
            } else {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    api.getClientHandler(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID)
                            .handleOutgoingPacket(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID,
                                    player,
                                    client.getNetworkHandler().getConnection(),
                                    false);
                });
            }
            button.visible = false;
            button.active = false;
            this.makeContractSignButton();
        }));
    }

    private void makeHireHuntersButton() {
        String hunterText = instance.getCoins() >= 1500 ? "Hire Hunters " + Formatting.GREEN + "(1500 gold)" : "Hire Hunters " + Formatting.RED + "(1500 gold)";
        if (this.hireHuntersButton != null) {
            this.hireHuntersButton.setMessage(new LiteralText(hunterText));
            return;
        }

        this.hireHuntersButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 23, 150, 20, new LiteralText(hunterText), (button) -> {
            if (instance.getCoins() >= 1500) {
                TaleOfKingdoms.getAPI().ifPresent(api -> {
                    Translations.SERVE.send(player);
                    if (MinecraftClient.getInstance().getServer() == null) {
                        api.getClientHandler(TaleOfKingdoms.HUNTER_PACKET_ID)
                                .handleOutgoingPacket(TaleOfKingdoms.HUNTER_PACKET_ID,
                                        player,
                                        client.getNetworkHandler().getConnection(),
                                        false);
                        return;
                    }

                    api.executeOnServer(() -> {
                        ServerWorld serverWorld = MinecraftClient.getInstance().getServer().getOverworld();
                        BlockPos blockPos = entity.getBlockPos();
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
