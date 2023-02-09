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
import com.convallyria.taleofkingdoms.common.entity.kingdom.ForemanEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ItemShopEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.KingdomVillagerEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.StockMarketEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleGuardianEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleMageEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleSoldierEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public class EntityTypes {

    private static final EntityDimensions HUMAN_ENTITY_DIMENSIONS = EntityDimensions.fixed(0.6f, 1.8f);

    public static final EntityType<FarmerEntity> FARMER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "farmer"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, FarmerEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildMasterEntity> GUILDMASTER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_master"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildMasterEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildMasterDefenderEntity> GUILDMASTER_DEFENDER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_master_defender"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildMasterDefenderEntity::new).fireImmune().dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<BlacksmithEntity> BLACKSMITH = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "blacksmith"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlacksmithEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<CityBuilderEntity> CITYBUILDER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "city_builder"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CityBuilderEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<KnightEntity> KNIGHT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "knight"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, KnightEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<InnkeeperEntity> INNKEEPER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "innkeeper"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, InnkeeperEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<HunterEntity> HUNTER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "hunter"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, HunterEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildGuardEntity> GUILDGUARD = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_guard"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildGuardEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildArcherEntity> GUILDARCHER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_archer"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildArcherEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<LoneEntity> LONE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "lone"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LoneEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<BankerEntity> BANKER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "banker"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BankerEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<FoodShopEntity> FOODSHOP = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "foodshop"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, FoodShopEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<GuildCaptainEntity> GUILDCAPTAIN = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "guild_captain"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GuildCaptainEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<LoneVillagerEntity> LONEVILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "lone_villager"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, LoneVillagerEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );

    public static final EntityType<ReficuleSoldierEntity> REFICULE_SOLDIER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "reficule_soldier"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ReficuleSoldierEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<ReficuleGuardianEntity> REFICULE_GUARDIAN = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "reficule_guardian"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ReficuleGuardianEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );
    public static final EntityType<ReficuleMageEntity> REFICULE_MAGE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "reficule_mage"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ReficuleMageEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );

    // =========================
    // Player's kingdom entities
    // =========================
    public static final EntityType<ItemShopEntity> ITEM_SHOP = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "item_shop"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ItemShopEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );

    public static final EntityType<KingdomVillagerEntity> KINGDOM_VILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "kingdom_villager"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, KingdomVillagerEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );

    public static final EntityType<StockMarketEntity> STOCK_MARKET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "stock_market"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, StockMarketEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );

    public static final EntityType<ForemanEntity> FOREMAN = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(TaleOfKingdoms.MODID, "foreman"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ForemanEntity::new).dimensions(HUMAN_ENTITY_DIMENSIONS).build()
    );

    public static final List<EntityType<? extends ShopEntity>> SHOP_ENTITIES = List.of(EntityTypes.BLACKSMITH, EntityTypes.ITEM_SHOP, EntityTypes.FOODSHOP);
}
