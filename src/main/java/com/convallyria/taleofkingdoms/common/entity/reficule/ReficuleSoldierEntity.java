package com.convallyria.taleofkingdoms.common.entity.reficule;

import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundGuildGoal;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildGuardEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ReficuleSoldierEntity extends TOKEntity {

    public ReficuleSoldierEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
        ItemStack ironSword = new ItemStack(Items.IRON_SWORD);
        ironSword.addEnchantment(Enchantments.MENDING, 1); // Want them to look fancy :)
        this.setStackInHand(Hand.MAIN_HAND, ironSword);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(3, new WanderAroundGuildGoal(this, 0.6D));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, LivingEntity.class, 100, true, true, livingEntity -> {
            return livingEntity instanceof PlayerEntity || livingEntity instanceof GuildGuardEntity; // Test whether guild guards are attacked
        }));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.6D, false));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() { // Slightly higher stats than guild guards.
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 75.0D) // Big increase! Needs balancing?
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 16.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
