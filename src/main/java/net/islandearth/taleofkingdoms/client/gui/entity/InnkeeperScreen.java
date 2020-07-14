package net.islandearth.taleofkingdoms.client.gui.entity;

import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.islandearth.taleofkingdoms.client.translation.Translations;
import net.islandearth.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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
                BlockPos rest = this.locateRestingPlace(instance.getStart(), instance.getEnd());
                if (rest != null) {
                    player.attemptTeleport(rest.getX(), rest.getY() + 1, rest.getZ(), true);
                    Timer timer2 = new Timer();
                    timer2.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            onClose();
                            player.trySleep(rest);
                        }
                    }, 20);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (player.isSleeping()) {
                                player.getEntityWorld().setDayTime(player.getEntityWorld().getDayTime() + 20);
                            } else {
                                this.cancel();
                            }
                        }
                    }, 40, 10);
                } else {
                    player.sendMessage(new StringTextComponent("House Keeper: It seems there are no rooms available at this time."));
                }
            }));

            this.addButton(new Button(this.width / 2 - 75, this.height / 2 - 13, 150, 20, "Wait for nighttime.", (button) -> {
                //TODO
            }));

            this.addButton(new Button(this.width / 2 - 75, this.height / 2 + 20, 150, 20, "Exit", (button) -> this.onClose()));
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
    private BlockPos locateRestingPlace(BlockPos start, BlockPos end) {
        List<BlockPos> validRest = new ArrayList<>();
        int topBlockX = (Math.max(start.getX(), end.getX()));
        int bottomBlockX = (Math.min(start.getX(), end.getX()));

        int topBlockY = (Math.max(start.getY(), end.getY()));
        int bottomBlockY = (Math.min(start.getY(), end.getY()));

        int topBlockZ = (Math.max(start.getZ(), end.getZ()));
        int bottomBlockZ = (Math.min(start.getZ(), end.getZ()));

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    TileEntity tileEntity = player.getEntityWorld().getChunkAt(blockPos).getTileEntity(blockPos);
                    if (tileEntity instanceof SignTileEntity) {
                        SignTileEntity signTileEntity = (SignTileEntity) tileEntity;
                        if (signTileEntity.getText(0).getFormattedText().equals("[Rest]")) {
                            validRest.add(blockPos);
                        }
                    }
                }
            }
        }

        if (validRest.isEmpty()) return null;
        Random rand = ThreadLocalRandom.current();
        return validRest.get(rand.nextInt(validRest.size()));
    }
}
