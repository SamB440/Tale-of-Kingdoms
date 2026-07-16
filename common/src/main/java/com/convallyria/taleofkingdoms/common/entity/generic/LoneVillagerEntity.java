package com.convallyria.taleofkingdoms.common.entity.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.MovementVaried;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.FollowPlayerGoal;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.PlayerTitle;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LoneVillagerEntity extends TOKEntity implements MovementVaried {

    private static final List<ResourceLocation> VALID_SKINS = List.of(
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

    private final ResourceLocation skin;

    public LoneVillagerEntity(@NotNull EntityType<? extends PathfinderMob> entityType, @NotNull Level world) {
        super(entityType, world);
        this.skin = VALID_SKINS.get(ThreadLocalRandom.current().nextInt(VALID_SKINS.size()));
    }

    @Override
    public Optional<ResourceLocation> getSkin() {
        return Optional.of(skin);
    }

    public boolean isMovementEnabled() {
        return movementEnabled;
    }

    public void setMovementEnabled(boolean movementEnabled) {
        this.movementEnabled = movementEnabled;
    }

    @Override
    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FollowPlayerGoal(this, 1.0F, 5, 30));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 30.0D);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) return InteractionResult.FAIL;
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api != null) {
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isPresent()
                && instance.get().isInGuild(this) && instance.get().getLoneVillagersWithRooms().contains(this.uuid)) {
                if (player.level().isClientSide()) Translations.LOST_VILLAGER_GUILD_THANK.send(player, PlayerTitle.of(player).getTranslation());
                return InteractionResult.PASS;
            }
        }

        this.setMovementEnabled(true);
        if (player.level().isClientSide()) Translations.LOST_VILLAGER_THANK.send(player);
        return InteractionResult.PASS;
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
