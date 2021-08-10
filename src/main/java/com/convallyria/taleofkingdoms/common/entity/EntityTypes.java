package com.convallyria.taleofkingdoms.common.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.KnightEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.LoneVillagerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.FarmerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.FoodShopEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildArcherEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildCaptainEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildGuardEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterDefenderEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.LoneEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleGuardianEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleMageEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleSoldierEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityTypes {

    private static final EntityDimensions HUMAN_ENTITY_DIMENSIONS = EntityDimensions.fixed(0.6f, 1.8f);

    public static final EntityType<FarmerEntity> FARMER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "farmer"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, FarmerEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildMasterEntity> GUILDMASTER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "guild_master"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, GuildMasterEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildMasterDefenderEntity> GUILDMASTER_DEFENDER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "guild_master_defender"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, GuildMasterDefenderEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<BlacksmithEntity> BLACKSMITH = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "blacksmith"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, BlacksmithEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<CityBuilderEntity> CITYBUILDER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "city_builder"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, CityBuilderEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<KnightEntity> KNIGHT = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "knight"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, KnightEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<InnkeeperEntity> INNKEEPER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "innkeeper"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, InnkeeperEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<HunterEntity> HUNTER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "hunter"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, HunterEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildGuardEntity> GUILDGUARD = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "guild_guard"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, GuildGuardEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildArcherEntity> GUILDARCHER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "guild_archer"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, GuildArcherEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<LoneEntity> LONE = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "lone"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, LoneEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<BankerEntity> BANKER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "banker"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, BankerEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<FoodShopEntity> FOODSHOP = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "foodshop"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, FoodShopEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildCaptainEntity> GUILDCAPTAIN = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "guild_captain"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, GuildCaptainEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<LoneVillagerEntity> LONEVILLAGER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "lone_villager"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, LoneVillagerEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );

    public static final EntityType<ReficuleSoldierEntity> REFICULE_SOLDIER = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "reficule_soldier"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, ReficuleSoldierEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<ReficuleGuardianEntity> REFICULE_GUARDIAN = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "reficule_guardian"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, ReficuleGuardianEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<ReficuleMageEntity> REFICULE_MAGE = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(TaleOfKingdoms.MODID, "reficule_mage"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, ReficuleMageEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
}
