package com.convallyria.taleofkingdoms.common.entity.nature.hostile;

import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.sounds.Sounds;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Random;

public class RatEntity extends HostileEntity {

    private static final Ingredient BREEDING_INGREDIENT;
    private static final ImmutableList<RegistryKey<Biome>> VALID_BIOME_SPAWNS = ImmutableList.of(BiomeKeys.FOREST,
            BiomeKeys.BIRCH_FOREST, BiomeKeys.BIRCH_FOREST_HILLS, BiomeKeys.TALL_BIRCH_FOREST,
            BiomeKeys.DARK_FOREST_HILLS, BiomeKeys.DARK_FOREST,
            BiomeKeys.FLOWER_FOREST,
            BiomeKeys.PLAINS,
            BiomeKeys.GIANT_SPRUCE_TAIGA, BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS,
            BiomeKeys.GIANT_TREE_TAIGA, BiomeKeys.GIANT_TREE_TAIGA_HILLS,
            BiomeKeys.TAIGA, BiomeKeys.TAIGA_HILLS, BiomeKeys.TAIGA_MOUNTAINS,
            BiomeKeys.SNOWY_TAIGA, BiomeKeys.SNOWY_TAIGA_HILLS, BiomeKeys.SNOWY_TAIGA_MOUNTAINS);

    public RatEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.25D, false));
        this.goalSelector.add(2, new TemptGoal(this, 1.2D, true, BREEDING_INGREDIENT));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));

        this.targetSelector.add(0, new FollowTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(1, new FollowTargetGoal<>(this, AnimalEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2D);
    }

    public static void spawnNaturally() {
        SpawnRestrictionAccessor.callRegister(
                EntityTypes.RAT,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                RatEntity::canMobSpawn
        );
        BiomeModifications.addSpawn(
                biomeSelectionContext -> VALID_BIOME_SPAWNS.contains(biomeSelectionContext.getBiomeKey()),
                SpawnGroup.CREATURE,
                EntityTypes.RAT,
                10,
                2,
                5
        );
    }

    public static boolean canMobSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockPos blockPos = pos.down();
        return HostileEntity.isSpawnDark((ServerWorldAccess) world, pos, random) && world.getBlockState(blockPos).allowsSpawning(world, blockPos, type);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Sounds.RAT_GRUNT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return Sounds.RAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Sounds.RAT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Vec3d method_29919() { // What does this do?
        return new Vec3d(0.0D, 0.6F * this.getStandingEyeHeight(), this.getWidth() * 0.4F);
    }

    static {
        BREEDING_INGREDIENT = Ingredient.ofItems(Items.BREAD);
    }
}
