package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.EnumSet;

public class CrossbowAttackGoal<T extends Mob & RangedAttackMob & CrossbowAttackMob> extends Goal {
    public static final UniformInt COOLDOWN_RANGE = TimeUtil.rangeOfSeconds(1, 2);
    private final T actor;
    private Stage stage;
    private final double speed;
    private final float squaredRange;
    private int seeingTargetTicker;
    private int chargedTicksLeft;
    private int cooldown;

    public CrossbowAttackGoal(T actor, double speed, float range) {
        this.stage = CrossbowAttackGoal.Stage.UNCHARGED;
        this.actor = actor;
        this.speed = speed;
        this.squaredRange = range * range;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        return this.hasAliveTarget() && this.isEntityHoldingCrossbow();
    }

    private boolean isEntityHoldingCrossbow() {
        return this.actor.isHolding(Items.CROSSBOW);
    }

    public boolean canContinueToUse() {
        return this.hasAliveTarget() && (this.canUse() || !this.actor.getNavigation().isDone()) && this.isEntityHoldingCrossbow();
    }

    private boolean hasAliveTarget() {
        return this.actor.getTarget() != null && this.actor.getTarget().isAlive();
    }

    public void stop() {
        super.stop();
        this.actor.setAggressive(false);
        this.actor.setTarget(null);
        this.seeingTargetTicker = 0;
        if (this.actor.isUsingItem()) {
            this.actor.stopUsingItem();
            this.actor.setChargingCrossbow(false);
            CrossbowItem.setCharged(this.actor.getUseItem(), false);
        }
    }

    public void tick() {
        LivingEntity livingEntity = this.actor.getTarget();
        if (livingEntity != null) {
            boolean bl = this.actor.getSensing().hasLineOfSight(livingEntity);
            boolean bl2 = this.seeingTargetTicker > 0;
            if (bl != bl2) {
                this.seeingTargetTicker = 0;
            }

            if (bl) {
                ++this.seeingTargetTicker;
            } else {
                --this.seeingTargetTicker;
            }

            double d = this.actor.distanceToSqr(livingEntity);
            boolean bl3 = (d > (double)this.squaredRange || this.seeingTargetTicker < 5) && this.chargedTicksLeft == 0;
            if (bl3) {
                --this.cooldown;
                if (this.cooldown <= 0) {
                    this.actor.getNavigation().moveTo(livingEntity, this.isUncharged() ? this.speed : this.speed * 0.5D);
                    this.cooldown = COOLDOWN_RANGE.sample(this.actor.getRandom());
                }
            } else {
                this.cooldown = 0;
                this.actor.getNavigation().stop();
            }

            this.actor.getLookControl().setLookAt(livingEntity, 30.0F, 30.0F);
            if (this.stage == Stage.UNCHARGED) {
                if (!bl3) {
                    this.actor.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.actor, Items.CROSSBOW));
                    this.stage = Stage.CHARGING;
                    this.actor.setChargingCrossbow(true);
                }
            } else if (this.stage == Stage.CHARGING) {
                if (!this.actor.isUsingItem()) {
                    this.stage = Stage.UNCHARGED;
                }

                int i = this.actor.getTicksUsingItem();
                ItemStack itemStack = this.actor.getUseItem();
                if (i >= CrossbowItem.getChargeDuration(itemStack)) {
                    this.actor.releaseUsingItem();
                    this.stage = Stage.CHARGED;
                    this.chargedTicksLeft = 20 + this.actor.getRandom().nextInt(20);
                    this.actor.setChargingCrossbow(false);
                }
            } else if (this.stage == Stage.CHARGED) {
                --this.chargedTicksLeft;
                if (this.chargedTicksLeft == 0) {
                    this.stage = Stage.READY_TO_ATTACK;
                }
            } else if (this.stage == Stage.READY_TO_ATTACK && bl) {
                this.actor.performRangedAttack(livingEntity, 1.0F);
                ItemStack itemStack2 = this.actor.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.actor, Items.CROSSBOW));
                CrossbowItem.setCharged(itemStack2, false);
                this.stage = Stage.UNCHARGED;
            }

        }
    }

    private boolean isUncharged() {
        return this.stage == CrossbowAttackGoal.Stage.UNCHARGED;
    }

    enum Stage {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;

        Stage() {
        }
    }
}
