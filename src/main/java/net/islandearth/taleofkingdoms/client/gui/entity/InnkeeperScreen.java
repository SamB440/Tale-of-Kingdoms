package net.islandearth.taleofkingdoms.client.gui.entity;

import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.islandearth.taleofkingdoms.client.translation.Translations;
import net.islandearth.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import net.islandearth.taleofkingdoms.common.utils.BlockUtils;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
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
        if (!instance.hasContract()) {
            Translations.NEED_CONTRACT.send(player);
            this.onClose();
        } else {
            this.addButton(new Button(this.width / 2 - 75, this.height / 4 + 50, 150, 20, "Rest in a room.", (button) -> {
                this.onClose();
                BlockPos rest = this.locateRestingPlace(player);
                if (rest != null) {
                    // Find a valid bedhead.
                    BlockPos bedHead = null;
                    for (BlockPos block : BlockUtils.getNearbyBlocks(rest, 3)) {
                        BlockState state = player.getEntityWorld().getBlockState(block);
                        if (state.isBed(player.getEntityWorld(), block, null)) {
                            bedHead = block;
                            break;
                        }
                    }

                    if (bedHead == null) {
                        player.sendMessage(new StringTextComponent("House Keeper: It seems there are no rooms available at this time."));
                        return;
                    }

                    player.teleportKeepLoaded(rest.getX() + 0.5, rest.getY(), rest.getZ() + 0.5);
                    player.trySleep(bedHead);
                } else {
                    player.sendMessage(new StringTextComponent("House Keeper: It seems there are no rooms available at this time."));
                }
            }));

            this.addButton(new Button(this.width / 2 - 75, this.height / 2 + 15, 150, 20, "Exit", (button) -> this.onClose()));
        }
    }

    @Override
    public void render(int par1, int par2, float par3) {
        super.render(par1, par2, par3);
        this.drawCenteredString(this.font, "Time flies when you rest...", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
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
