package com.convallyria.taleofkingdoms.common.entity.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.MovementVaried;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.FollowPlayerGoal;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LoneVillagerEntity extends TOKEntity implements MovementVaried {

    private boolean movementEnabled;

    public LoneVillagerEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
    }

    public boolean isMovementEnabled() {
        return movementEnabled;
    }

    public void setMovementEnabled(boolean movementEnabled) {
        this.movementEnabled = movementEnabled;
    }

    @Override
    public void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new FollowPlayerGoal(this, 1.0F, 5, 30));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30.0D);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        Optional<TaleOfKingdomsAPI> api = TaleOfKingdoms.getAPI();
        if (api.isPresent()) {
            Optional<ConquestInstance> instance = api.get().getConquestInstanceStorage().mostRecentInstance();
            if (instance.isPresent()
                && instance.get().isInGuild(this) && instance.get().getLoneVillagersWithRooms().contains(this.uuid)) {
                return ActionResult.FAIL;
            }
        }

        this.setMovementEnabled(true);
        if (hand == Hand.OFF_HAND || !player.world.isClient()) return ActionResult.FAIL;
        Translations.LOST_VILLAGER_THANK.send(player);
        return ActionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return this.isMovementEnabled();
    }

    @Override
    public boolean isPushable() {
        return this.isMovementEnabled();
    }
}
