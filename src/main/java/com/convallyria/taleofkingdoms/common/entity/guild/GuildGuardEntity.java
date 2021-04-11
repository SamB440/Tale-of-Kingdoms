package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundGuildGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class GuildGuardEntity extends TOKEntity {

    public GuildGuardEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new WanderAroundGuildGoal(this, 0.6D));
        this.targetSelector.add(1, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_SOLDIER, false));
        this.targetSelector.add(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_GUARDIAN, false));
        this.targetSelector.add(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_MAGE, false));
        this.targetSelector.add(4, new FollowTargetGoal<>(this, MobEntity.class, 100,
                true, true, livingEntity -> livingEntity instanceof Monster));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.6D, false));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 30.0F));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);

    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || !player.world.isClient()) return ActionResult.FAIL;
        //TODO
        Translations.GUILDMEMBER_START.send(player);
        return ActionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
