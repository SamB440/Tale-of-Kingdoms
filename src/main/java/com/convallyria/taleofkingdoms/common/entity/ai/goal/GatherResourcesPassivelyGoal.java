package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ForemanEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.workers.QuarryWorkerEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GatherResourcesPassivelyGoal extends Goal {

    private final Mob mob;

    public GatherResourcesPassivelyGoal(Mob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return ThreadLocalRandom.current().nextInt(100) > 75;
    }

    @Override
    public void tick() {
        if (ThreadLocalRandom.current().nextInt(100) > 98) {
            mob.swing(InteractionHand.MAIN_HAND);
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                for (GuildPlayer guildPlayer : instance.getGuildPlayers().values()) {
                    final PlayerKingdom kingdom = guildPlayer.getKingdom();
                    if (kingdom == null || !kingdom.isInKingdom(mob.blockPosition())) continue;
                    final EntityType<? extends ForemanEntity> type = mob instanceof QuarryWorkerEntity ? EntityTypes.QUARRY_FOREMAN : EntityTypes.LUMBER_FOREMAN;
                    kingdom.getKingdomEntity(mob.level(), type).ifPresent(foreman -> {
                        foreman.getInventory().addItem(new ItemStack(mob instanceof QuarryWorkerEntity ? Items.COBBLESTONE : Items.OAK_LOG, 1));
                        //todo check server compat
                    });
                    break;
                }
            });
        }
    }
}
