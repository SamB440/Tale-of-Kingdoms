package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.gui.entity.GuildMasterScreen;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;

public class GuildMasterEntity extends TOKEntity {
    public GuildMasterEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND) return ActionResult.FAIL;
        TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();

        if (player instanceof ServerPlayerEntity) return ActionResult.FAIL;
        if (api.getEnvironment() == EnvType.CLIENT) this.openScreen(player, instance);
        return ActionResult.PASS;
    }

    @Environment(EnvType.CLIENT)
    private void openScreen(PlayerEntity player, ConquestInstance instance) {
        GuildMasterScreen screen = new GuildMasterScreen(player, this, instance);
        MinecraftClient.getInstance().setScreen(screen);
    }

    @Override
    public boolean isFireImmune() {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api != null) {
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isPresent()) {
                return instance.get().isUnderAttack();
            }
        }
        return false;
    }

    @Override
    public boolean damage(DamageSource damageSource, float f) {
        return !this.getType().equals(EntityTypes.GUILDMASTER);
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}