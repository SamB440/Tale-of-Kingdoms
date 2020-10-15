package net.islandearth.taleofkingdoms.client.gui.entity;

import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.islandearth.taleofkingdoms.client.translation.Translations;
import net.islandearth.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class InnkeeperScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final InnkeeperEntity entity;
    private final ConquestInstance instance;

    public InnkeeperScreen(PlayerEntity player, InnkeeperEntity entity, ConquestInstance instance) {
        super("taleofkingdoms.menu.innkeeper.name");
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        Translations.INNKEEPER_REST.send(player);
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 4 + 50, 150, 20, new LiteralText("Rest in a room."), (button) -> {
            this.onClose();
            //TODO fix sleeping for fabric
            BlockPos rest = this.locateRestingPlace(player);
            if (rest != null) {
                MinecraftClient.getInstance().getServer().getOverworld().setTimeOfDay(24000);
                MinecraftClient.getInstance().getServer().execute(() -> {
                    ServerPlayerEntity serverPlayerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
                    serverPlayerEntity.refreshPositionAfterTeleport(rest.getX() + 0.5, rest.getY(), rest.getZ() + 0.5);
                    serverPlayerEntity.applyStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1));
                    serverPlayerEntity.applyStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
                });
                //player.sleep(rest);
            } else {
                player.sendMessage(new LiteralText("House Keeper: It seems there are no rooms available at this time."), false);
            }
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 75, this.height / 2 + 15, 150, 20, new LiteralText("Exit"), (button) -> this.onClose()));
    }

    @Override
    public void render(MatrixStack stack, int par1, int par2, float par3) {
        super.render(stack, par1, par2, par3);
        drawCenteredString(stack, this.textRenderer, "Time flies when you rest...", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        super.onClose();
        Translations.INNKEEPER_LEAVE.send(player);
    }

    @Nullable
    private BlockPos locateRestingPlace(PlayerEntity player) {
        List<BlockPos> validRest = instance.getSleepLocations(player);

        if (validRest.isEmpty()) return null;
        Random rand = ThreadLocalRandom.current();
        return validRest.get(rand.nextInt(validRest.size()));
    }
}
