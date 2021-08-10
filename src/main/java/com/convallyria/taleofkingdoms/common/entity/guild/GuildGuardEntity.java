package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundGuildGoal;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class GuildGuardEntity extends TOKEntity {

    public GuildGuardEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.6D, false));
        this.goalSelector.addGoal(2, new WanderAroundGuildGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 30.0F));
        this.targetSelector.addGoal(1, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_SOLDIER, false));
        this.targetSelector.addGoal(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_GUARDIAN, false));
        this.targetSelector.addGoal(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_MAGE, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 100,
                true, true, livingEntity -> livingEntity instanceof Enemy));
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 35)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D);
    }

    @Override
    protected InteractionResult mobInteract(final Player player, final InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) return InteractionResult.FAIL;
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                if (instance.hasAttacked(player.getUUID())) {
                    if (player.getMainHandItem().getItem() == Items.WOODEN_SWORD) {
                        this.setItemInHand(InteractionHand.MAIN_HAND, player.getMainHandItem());
                        if (player.level.isClientSide()) Translations.GUILDMEMBER_START_FIGHT.send(player);
                        final int[] countdown = {3};
                        api.getScheduler().repeatN(server -> {
                            player.displayClientMessage(new TextComponent("" + countdown[0]), false);
                            countdown[0] = countdown[0] - 1;
                        }, 3, 0, 20);
                        api.getScheduler().queue(server -> {
                            final ImprovedFollowTargetGoal<Player> goal = new ImprovedFollowTargetGoal<>(this, EntityType.PLAYER, true);
                            this.targetSelector.addGoal(0, goal);
                            if (player.level.isClientSide()) Translations.GUILDMEMBER_BEGIN.send(player);
                            api.getScheduler().queue(server2 -> {
                                this.targetSelector.removeGoal(goal);
                                if (player.level.isClientSide()) Translations.GUILDMEMBER_GOOD_FIGHTER.send(player);
                                instance.addWorthiness(player.getUUID(), 2);
                                this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
                            }, 160);
                        }, 80);
                        player.getInventory().removeItem(player.getMainHandItem());
                        return;
                    }
                    if (player.level.isClientSide()) Translations.GUILDMEMBER_FIGHTER.send(player);
                } else {
                    if (player.level.isClientSide()) Translations.GUILDMEMBER_START.send(player);
                }
            });
        });
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
