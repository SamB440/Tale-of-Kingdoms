package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenBar;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class GuildMasterScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final GuildMasterEntity entity;
    private final ClientConquestInstance instance;
    private ScreenBar worthness;

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
            this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 45, 150, 20, Translations.GUILDMASTER_CONTRACT_SIGN_UP.getTranslation(), (button) -> {
                instance.setHasContract(true);
                Translations.GUILDMASTER_CONTRACT_SIGN.send(player);
                button.visible = false;
                button.active = false;
                this.onClose();
                MinecraftClient.getInstance().openScreen(new GuildMasterScreen(player, entity, instance));
            }));
        } else {
            this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 45, 150, 20, Translations.GUILDMASTER_CONTRACT_CANCEL.getTranslation(), (button) -> {
                instance.setHasContract(false);
                button.visible = false;
                button.active = false;
            }));
        }

        String hunterText = instance.getCoins() >= 1500 ? "Hire Hunters " + Formatting.GREEN + "(1500 gold)" : "Hire Hunters " + Formatting.RED + "(1500 gold)";
        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 2 - 13, 150, 20, new LiteralText(hunterText), (button) -> {
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

        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 2 + 20, 150, 20, new LiteralText("Exit"), (button) -> {
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
