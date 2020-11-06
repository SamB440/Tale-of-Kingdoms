package net.islandearth.taleofkingdoms.common.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.entity.generic.HunterEntity;
import net.islandearth.taleofkingdoms.common.entity.generic.KnightEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.FarmerEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.GuildGuardEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.InnkeeperEntity;
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
    public static final EntityType<GuildGuardEntity> GUARD = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_guard"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildGuardEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
}
