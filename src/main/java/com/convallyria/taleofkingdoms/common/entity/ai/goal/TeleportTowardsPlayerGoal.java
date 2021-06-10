package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.common.entity.reficule.TeleportAbility;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TeleportTowardsPlayerGoal extends FollowTargetGoal<PlayerEntity> {

    private final MobEntity entity;
    private PlayerEntity targetPlayer;
    private final TargetPredicate predicate;
    private long lastTick = 1;

    public TeleportTowardsPlayerGoal(TeleportAbility entity, @Nullable Predicate<LivingEntity> predicate) {
        super((MobEntity) entity, PlayerEntity.class, 10, false, false, predicate);
        this.entity = (MobEntity) entity;
        this.predicate = (TargetPredicate.createAttackable()).setBaseMaxDistance(this.getFollowRange()).setPredicate(pred -> true);
    }

    @Override
    public boolean canStart() {
        this.lastTick++;
        this.targetPlayer = this.entity.world.getClosestPlayer(this.predicate, this.entity);
        return this.targetPlayer != null && lastTick > 100;
    }

    @Override
    public void tick() {
        if (targetPlayer != null) {
            int rand = this.mob.getRandom().nextInt(5);
            if (rand == 2 && this.targetPlayer.squaredDistanceTo(this.entity) < this.getFollowRange()) {
                TeleportAbility teleportAbility = (TeleportAbility) this.entity;
                teleportAbility.teleportTo(targetPlayer);
                this.lastTick = 0;
                if (teleportAbility.spreadFire()) {
                    int spawnFire = this.mob.getRandom().nextInt(6);
                    if (spawnFire == 1) {
                        for (BlockPos blockPos : BlockUtils.getNearbyBlocksUnder(this.mob.getBlockPos(), 1)) {
                            BlockPos up = blockPos.add(0, 1, 0);
                            if (!this.mob.world.getBlockState(up).isAir()) {
                                continue;
                            }
                            this.mob.world.setBlockState(up, Blocks.FIRE.getDefaultState());
                        }
                    }
                }
            }
        }
        super.tick();
    }
}