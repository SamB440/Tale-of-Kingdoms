package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.MultiSkinned;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class HumanFarmerEntity extends VillagerEntity implements MultiSkinned {

    private static final List<Identifier> VALID_SKINS = List.of(
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/tok_farmer.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/innkeeper.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/tok_farmer_3.png")
    );

    private final Identifier skin;

    public HumanFarmerEntity(EntityType<? extends VillagerEntity> entityType, World world) {
        super(entityType, world);
        this.setVillagerData(this.getVillagerData().withProfession(VillagerProfession.FARMER));
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.WOODEN_HOE));
        this.skin = VALID_SKINS.get(ThreadLocalRandom.current().nextInt(VALID_SKINS.size()));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        this.setVillagerData(this.getVillagerData().withProfession(VillagerProfession.FARMER));
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.WOODEN_HOE));
        return data;
    }

    @Override
    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (sound.getId().getPath().toLowerCase(Locale.ROOT).contains("villager")) return;
        super.playSound(sound, volume, pitch);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        return ActionResult.PASS;
    }

    @Override
    public boolean wantsToStartBreeding() {
        return false;
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.tickHandSwing();
    }

    @Override
    public Optional<Identifier> getSkin() {
        return Optional.of(skin);
    }
}
