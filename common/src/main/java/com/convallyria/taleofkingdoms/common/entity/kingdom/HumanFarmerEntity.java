package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.MultiSkinned;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class HumanFarmerEntity extends Villager implements MultiSkinned {

    private static final List<ResourceLocation> VALID_SKINS = List.of(
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "textures/entity/updated_textures/tok_farmer.png"),
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "textures/entity/updated_textures/innkeeper.png"),
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "textures/entity/updated_textures/tok_farmer_3.png")
    );

    private final ResourceLocation skin;

    public HumanFarmerEntity(EntityType<? extends Villager> entityType, Level world) {
        super(entityType, world);
        this.setVillagerData(this.getVillagerData().setProfession(VillagerProfession.FARMER));
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WOODEN_HOE));
        this.skin = VALID_SKINS.get(ThreadLocalRandom.current().nextInt(VALID_SKINS.size()));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
        this.setVillagerData(this.getVillagerData().setProfession(VillagerProfession.FARMER));
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WOODEN_HOE));
        return data;
    }

    @Override
    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (sound.getLocation().getPath().toLowerCase(Locale.ROOT).contains("villager")) return;
        super.playSound(sound, volume, pitch);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean hasExcessFood() {
        return false;
    }

    @Override
    public boolean wantsMoreFood() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.updateSwingTime();
    }

    @Override
    public Optional<ResourceLocation> getSkin() {
        return Optional.of(skin);
    }
}
