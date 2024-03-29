package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundGuildGoal;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
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
import net.minecraft.text.Text;
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
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, MobEntity.class, 100,
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
        final boolean client = player.getWorld().isClient();
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            if (instance.hasAttacked(player.getUuid())) {
                if (player.getMainHandStack().getItem() == Items.WOODEN_SWORD) {
                    this.setStackInHand(Hand.MAIN_HAND, player.getMainHandStack());
                    if (!client) Translations.GUILDMEMBER_START_FIGHT.send(player);
                    final int[] countdown = {3};
                    api.getScheduler().repeatN(server -> {
                        boolean send = FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT || !client;
                        if (send) player.sendMessage(Text.literal("" + countdown[0]), false);
                        countdown[0] = countdown[0] - 1;
                    }, 3, 0, 20);
                    api.getScheduler().queue(server -> {
                        final ImprovedFollowTargetGoal<PlayerEntity> goal = new ImprovedFollowTargetGoal<>(this, EntityType.PLAYER, true);
                        this.targetSelector.add(0, goal);
                        if (!client) Translations.GUILDMEMBER_BEGIN.send(player);
                        api.getScheduler().queue(server2 -> {
                            this.targetSelector.remove(goal);
                            if (!client) Translations.GUILDMEMBER_GOOD_FIGHTER.send(player);
                            final GuildPlayer guildPlayer = instance.getPlayer(player);
                            guildPlayer.setWorthiness(guildPlayer.getWorthiness() + 2);
                            this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
                        }, 160);
                    }, 80);
                    player.getInventory().removeOne(player.getMainHandStack());
                    return;
                }
                if (!client) Translations.GUILDMEMBER_FIGHTER.send(player);
            } else {
                if (!client) Translations.GUILDMEMBER_START.send(player);
            }
        });
        return ActionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
