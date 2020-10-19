package net.islandearth.taleofkingdoms.common.entity.guild;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.entity.ai.goal.WanderAroundGuildGoal;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
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
        this.targetSelector.add(1, new FollowTargetGoal<>(this, MobEntity.class, 100, true, true, livingEntity -> {
            return livingEntity instanceof Monster;
        }));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.5D, false));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
        this.goalSelector.add(3, new WanderAroundGuildGoal(this, 0.5D));
        applyEntityAI();
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || !player.world.isClient()) return ActionResult.FAIL;
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        //TODO
        return ActionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
