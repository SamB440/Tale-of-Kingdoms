package com.convallyria.taleofkingdoms.common.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.KnightEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.LoneVillagerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.*;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleGuardianEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleMageEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleSoldierEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityTypes {

    private static final EntityDimensions HUMAN_ENTITY_DIMENSIONS = EntityDimensions.fixed(0.6f, 1.8f);

    public static final EntityType<FarmerEntity> FARMER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "farmer"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, FarmerEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildMasterEntity> GUILDMASTER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_master"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildMasterEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildMasterDefenderEntity> GUILDMASTER_DEFENDER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_master_defender"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildMasterDefenderEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<BlacksmithEntity> BLACKSMITH = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "blacksmith"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlacksmithEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<CityBuilderEntity> CITYBUILDER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "city_builder"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CityBuilderEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<KnightEntity> KNIGHT = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "knight"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, KnightEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<InnkeeperEntity> INNKEEPER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "innkeeper"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, InnkeeperEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<HunterEntity> HUNTER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "hunter"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HunterEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildGuardEntity> GUILDGUARD = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_guard"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildGuardEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildArcherEntity> GUILDARCHER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_archer"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildArcherEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<LoneEntity> LONE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "lone"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LoneEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<BankerEntity> BANKER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "banker"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BankerEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<FoodShopEntity> FOODSHOP = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "foodshop"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, FoodShopEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildCaptainEntity> GUILDCAPTAIN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_captain"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildCaptainEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<LoneVillagerEntity> LONEVILLAGER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "lone_villager"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LoneVillagerEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );

    public static final EntityType<ReficuleSoldierEntity> REFICULE_SOLDIER = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "reficule_soldier"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ReficuleSoldierEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<ReficuleGuardianEntity> REFICULE_GUARDIAN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "reficule_guardian"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ReficuleGuardianEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<ReficuleMageEntity> REFICULE_MAGE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "reficule_mage"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ReficuleMageEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
}
