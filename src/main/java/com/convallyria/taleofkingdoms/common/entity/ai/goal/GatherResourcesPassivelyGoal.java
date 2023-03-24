package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ForemanEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.QuarryWorkerEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.concurrent.ThreadLocalRandom;

public class GatherResourcesPassivelyGoal extends Goal {

    private final MobEntity mob;

    public GatherResourcesPassivelyGoal(MobEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return ThreadLocalRandom.current().nextInt(100) > 75;
    }

    @Override
    public void tick() {
        mob.swingHand(mob.getActiveHand(), true); //todo: this doesn't work and i don't know why
        if (ThreadLocalRandom.current().nextInt(100) > 98) {
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                for (GuildPlayer guildPlayer : instance.getGuildPlayers().values()) {
                    final PlayerKingdom kingdom = guildPlayer.getKingdom();
                    if (kingdom == null || !kingdom.isInKingdom(mob.getBlockPos())) continue;
                    final EntityType<? extends ForemanEntity> type = mob instanceof QuarryWorkerEntity ? EntityTypes.QUARRY_FOREMAN : EntityTypes.LUMBER_FOREMAN;
                    kingdom.getKingdomEntity(mob.world, type).ifPresent(foreman -> {
                        foreman.getInventory().addStack(new ItemStack(mob instanceof QuarryWorkerEntity ? Items.COBBLESTONE : Items.OAK_LOG, 1));
                        //todo check server compat
                    });
                    break;
                }
            });
        }
    }
}
