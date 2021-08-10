package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.common.entity.reficule.TeleportAbility;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TeleportTowardsPlayerGoal extends NearestAttackableTargetGoal<Player> {

    private final Mob entity;
    private Player targetPlayer;
    private final TargetingConditions predicate;
    private long lastTick = 1;

    public TeleportTowardsPlayerGoal(TeleportAbility entity, @Nullable Predicate<LivingEntity> predicate) {
        super((Mob) entity, Player.class, 10, false, false, predicate);
        this.entity = (Mob) entity;
        this.predicate = (TargetingConditions.forCombat()).range(this.getFollowDistance()).selector(pred -> true);
    }

    @Override
    public boolean canUse() {
        this.lastTick++;
        this.targetPlayer = this.entity.level.getNearestPlayer(this.predicate, this.entity);
        return this.targetPlayer != null && lastTick > 100;
    }

    @Override
    public void tick() {
        if (targetPlayer != null) {
            int rand = this.mob.getRandom().nextInt(5);
            if (rand == 2 && this.targetPlayer.distanceToSqr(this.entity) < this.getFollowDistance()) {
                TeleportAbility teleportAbility = (TeleportAbility) this.entity;
                teleportAbility.teleportTo(targetPlayer);
                this.lastTick = 0;
                if (teleportAbility.spreadFire()) {
                    int spawnFire = this.mob.getRandom().nextInt(6);
                    if (spawnFire == 1) {
                        for (BlockPos blockPos : BlockUtils.getNearbyBlocksUnder(this.mob.blockPosition(), 1)) {
                            BlockPos up = blockPos.offset(0, 1, 0);
                            if (!this.mob.level.getBlockState(up).isAir()) {
                                continue;
                            }
                            this.mob.level.setBlockAndUpdate(up, Blocks.FIRE.defaultBlockState());
                        }
                    }
                }
            }
        }
        super.tick();
    }
}