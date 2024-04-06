package com.convallyria.taleofkingdoms.common.entity.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.translation.Translations;
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
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class LoneVillagerEntity extends TOKEntity implements MovementVaried {

    private static final List<Identifier> VALID_SKINS = List.of(
            identifier("textures/entity/updated_textures/lostvillagerone.png"),
            identifier("textures/entity/updated_textures/lostvillagertwo.png"),
            identifier("textures/entity/updated_textures/lostvillagerthree.png"),
            identifier("textures/entity/updated_textures/lostvillagerfour.png"),
            identifier("textures/entity/updated_textures/lostvillagerfive.png"),
            identifier("textures/entity/updated_textures/lostvillagersix.png"),
            identifier("textures/entity/updated_textures/lostvillagerseven.png"),
            identifier("textures/entity/updated_textures/manone.png"),
            identifier("textures/entity/updated_textures/mantwo.png"),
            identifier("textures/entity/updated_textures/manfive.png")
    );

    private boolean movementEnabled;

    private final Identifier skin;

    public LoneVillagerEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
        this.skin = VALID_SKINS.get(ThreadLocalRandom.current().nextInt(VALID_SKINS.size()));
    }

    @Override
    public Optional<Identifier> getSkin() {
        return Optional.of(skin);
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
        if (hand == Hand.OFF_HAND) return ActionResult.FAIL;
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api != null) {
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isPresent()
                && instance.get().isInGuild(this) && instance.get().getLoneVillagersWithRooms().contains(this.uuid)) {
                if (player.getWorld().isClient()) Translations.LOST_VILLAGER_GUILD_THANK.send(player);
                return ActionResult.PASS;
            }
        }

        this.setMovementEnabled(true);
        if (player.getWorld().isClient()) Translations.LOST_VILLAGER_THANK.send(player);
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
