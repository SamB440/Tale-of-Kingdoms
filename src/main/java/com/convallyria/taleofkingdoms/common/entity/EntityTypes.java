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
import com.convallyria.taleofkingdoms.common.entity.guild.GuildCaptainEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildGuardEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.LoneEntity;
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
}
