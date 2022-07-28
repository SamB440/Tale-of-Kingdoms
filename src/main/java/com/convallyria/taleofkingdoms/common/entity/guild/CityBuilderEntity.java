package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.cotton.CityBuilderBeginGui;
import com.convallyria.taleofkingdoms.client.gui.entity.cotton.CityBuilderBeginScreen;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.FollowPlayerGoal;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CityBuilderEntity extends TOKEntity {

    private static final TrackedData<Boolean> MOVING_TO_LOCATION;

    static {
        MOVING_TO_LOCATION = DataTracker.registerData(CityBuilderEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MOVING_TO_LOCATION, false);
    }

    public CityBuilderEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND) return ActionResult.FAIL;
        TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            if (this.getDataTracker().get(MOVING_TO_LOCATION)) {
                BlockPos current = this.getBlockPos();
                int distance = (int) instance.getCentre().distanceTo(new Vec3d(current.getX(), current.getY(), current.getZ()));
                if (distance < (3000)) {
                    Translations.CITYBUILDER_DISTANCE.send(player, distance, 3000);
                    return;
                }

                if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.CLIENT) {
                    openScreen(player, instance);
                }
                return;
            }

            if (instance.getWorthiness(player.getUuid()) >= 1500) {
                Translations.CITYBUILDER_BUILD.send(player);
                this.goalSelector.add(2, new FollowPlayerGoal(this, 0.75F, 5, 50));
                this.getDataTracker().set(MOVING_TO_LOCATION, true);
            } else {
                Translations.CITYBUILDER_MESSAGE.send(player);
            }
        });
        return ActionResult.PASS;
    }

    @Environment(EnvType.CLIENT)
    private void openScreen(PlayerEntity player, ConquestInstance instance) {
        MinecraftClient.getInstance().setScreen(new CityBuilderBeginScreen(new CityBuilderBeginGui()));
    }

    @Override
    public boolean isStationary() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean damage(DamageSource damageSource, float f) {
        return false;
    }
}
