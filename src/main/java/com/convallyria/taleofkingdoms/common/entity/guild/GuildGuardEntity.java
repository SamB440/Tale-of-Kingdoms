package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundGuildGoal;
import net.minecraft.client.network.ClientPlayerEntity;
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
import net.minecraft.text.LiteralText;
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
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.6D, false));
        this.goalSelector.add(2, new WanderAroundGuildGoal(this, 0.6D));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 30.0F));
        this.targetSelector.add(1, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_SOLDIER, false));
        this.targetSelector.add(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_GUARDIAN, false));
        this.targetSelector.add(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_MAGE, false));
        this.targetSelector.add(4, new FollowTargetGoal<>(this, MobEntity.class, 100,
                true, true, livingEntity -> livingEntity instanceof Monster));
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 35)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
    }

    @Override
    protected ActionResult interactMob(final PlayerEntity player, final Hand hand) {
        if (hand == Hand.OFF_HAND) return ActionResult.FAIL;
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                if (instance.hasAttacked()) {
                    if (player.getMainHandStack().getItem() == Items.WOODEN_SWORD) {
                        if (player instanceof ClientPlayerEntity)  Translations.GUILDMEMBER_START_FIGHT.send(player);
                        final int[] countdown = {3};
                        api.getScheduler().repeatN(server -> {
                            if (player instanceof ClientPlayerEntity) player.sendMessage(new LiteralText("" + countdown[0]), false);
                            countdown[0] = countdown[0] - 1;
                        }, 3, 0, 20);
                        api.getScheduler().queue(server -> {
                            final ImprovedFollowTargetGoal<PlayerEntity> goal = new ImprovedFollowTargetGoal<>(this, EntityType.PLAYER, true);
                            this.targetSelector.add(0, goal);
                            if (player instanceof ClientPlayerEntity) Translations.GUILDMEMBER_BEGIN.send(player);
                            api.getScheduler().queue(server2 -> {
                                this.targetSelector.remove(goal);
                                if (player instanceof ClientPlayerEntity) Translations.GUILDMEMBER_GOOD_FIGHTER.send(player);
                            }, 160);
                        }, 80);
                        return;
                    }
                    if (player instanceof ClientPlayerEntity) Translations.GUILDMEMBER_FIGHTER.send(player);
                } else {
                    if (player instanceof ClientPlayerEntity) Translations.GUILDMEMBER_START.send(player);
                }
            });
        });
        return ActionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
