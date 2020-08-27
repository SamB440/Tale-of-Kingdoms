package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.event.BlockBreakCallback;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class BlockListener extends Listener {

    public BlockListener() {
        BlockBreakCallback.EVENT.register((block, pos) -> {
            if (MinecraftClient.getInstance().getServer() == null) return ActionResult.FAIL;
            Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance();
            if (instance.isPresent()) {
                BlockPos remove = null;
                for (BlockPos blockPos : instance.get().getValidRest()) {
                    if (blockPos.getX() == pos.getX() && blockPos.getY() == pos.getY() && blockPos.getZ() == pos.getZ()) {
                        remove = blockPos;
                        break;
                    }
                }

                if (remove != null) {
                    instance.get().getValidRest().remove(remove);
                }
            }

            return ActionResult.PASS;
        });
    }
}
