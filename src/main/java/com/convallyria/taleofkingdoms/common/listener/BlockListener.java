package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.event.BlockBreakCallback;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class BlockListener extends Listener {

    public BlockListener() {
        BlockBreakCallback.EVENT.register((block, pos) -> {
            if (MinecraftClient.getInstance().getServer() == null) return ActionResult.FAIL;
            Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance();
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
