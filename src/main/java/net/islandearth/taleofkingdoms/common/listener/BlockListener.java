package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class BlockListener extends Listener {

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        if (Minecraft.getInstance().getIntegratedServer() == null) return;
        Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName());
        if (instance.isPresent()) {
            BlockPos broken = event.getPos();
            BlockPos remove = null;
            for (BlockPos blockPos : instance.get().getValidRest()) {
                if (blockPos.getX() == broken.getX() && blockPos.getY() == broken.getY() && blockPos.getZ() == broken.getZ()) {
                    remove = blockPos;
                    break;
                }
            }

            if (remove != null) {
                instance.get().getValidRest().remove(remove);
            }
        }
    }
}
