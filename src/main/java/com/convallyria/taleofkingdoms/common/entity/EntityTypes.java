package com.convallyria.taleofkingdoms.common.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.generic.BanditEntity;
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
import com.convallyria.taleofkingdoms.common.entity.guild.GuildVillagerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.LoneEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.BlockShopEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.HumanFarmerEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ItemShopEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.KingdomVillagerEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.StockMarketEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.warden.ArcherHireableEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.warden.WardenEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.warden.WarriorHireableEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.workers.LumberForemanEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.workers.LumberWorkerEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.workers.QuarryForemanEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.workers.QuarryWorkerEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleGuardianEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleMageEntity;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleSoldierEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public class EntityTypes {

    private static final float HUMAN_WIDTH = 0.6f;
    private static final float HUMAN_HEIGHT = 1.8f;

    public static final EntityType<FarmerEntity> FARMER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "farmer"),
            EntityType.Builder.create(FarmerEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildMasterEntity> GUILDMASTER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "guild_master"),
            EntityType.Builder.create(GuildMasterEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildMasterDefenderEntity> GUILDMASTER_DEFENDER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "guild_master_defender"),
            EntityType.Builder.create(GuildMasterDefenderEntity::new, SpawnGroup.MISC).makeFireImmune().dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<BlacksmithEntity> BLACKSMITH = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "blacksmith"),
            EntityType.Builder.create(BlacksmithEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<CityBuilderEntity> CITYBUILDER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "city_builder"),
            EntityType.Builder.create(CityBuilderEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<KnightEntity> KNIGHT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "knight"),
            EntityType.Builder.create(KnightEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<InnkeeperEntity> INNKEEPER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "innkeeper"),
            EntityType.Builder.create(InnkeeperEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<HunterEntity> HUNTER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "hunter"),
            EntityType.Builder.create(HunterEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildGuardEntity> GUILDGUARD = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "guild_guard"),
            EntityType.Builder.create(GuildGuardEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildVillagerEntity> GUILDVILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "guild_villager"),
            EntityType.Builder.create(GuildVillagerEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildArcherEntity> GUILDARCHER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "guild_archer"),
            EntityType.Builder.create(GuildArcherEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<LoneEntity> LONE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "lone"),
            EntityType.Builder.create(LoneEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<BankerEntity> BANKER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "banker"),
            EntityType.Builder.create(BankerEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<FoodShopEntity> FOODSHOP = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "foodshop"),
            EntityType.Builder.create(FoodShopEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildCaptainEntity> GUILDCAPTAIN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "guild_captain"),
            EntityType.Builder.create(GuildCaptainEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<LoneVillagerEntity> LONEVILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "lone_villager"),
            EntityType.Builder.create(LoneVillagerEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<ReficuleSoldierEntity> REFICULE_SOLDIER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "reficule_soldier"),
            EntityType.Builder.create(ReficuleSoldierEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<ReficuleGuardianEntity> REFICULE_GUARDIAN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "reficule_guardian"),
            EntityType.Builder.create(ReficuleGuardianEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<ReficuleMageEntity> REFICULE_MAGE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "reficule_mage"),
            EntityType.Builder.create(ReficuleMageEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<BanditEntity> BANDIT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "bandit"),
            EntityType.Builder.create(BanditEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    // =========================
    // Player's kingdom entities
    // =========================
    public static final EntityType<ItemShopEntity> ITEM_SHOP = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "item_shop"),
            EntityType.Builder.create(ItemShopEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<KingdomVillagerEntity> KINGDOM_VILLAGER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "kingdom_villager"),
            EntityType.Builder.create(KingdomVillagerEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<StockMarketEntity> STOCK_MARKET = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "stock_market"),
            EntityType.Builder.create(StockMarketEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<QuarryForemanEntity> QUARRY_FOREMAN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "quarry_foreman"),
            EntityType.Builder.create(QuarryForemanEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<LumberForemanEntity> LUMBER_FOREMAN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "lumber_foreman"),
            EntityType.Builder.create(LumberForemanEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<QuarryWorkerEntity> QUARRY_WORKER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "quarry_worker"),
            EntityType.Builder.create(QuarryWorkerEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<LumberWorkerEntity> LUMBER_WORKER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "lumber_worker"),
            EntityType.Builder.create(LumberWorkerEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<WardenEntity> WARDEN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "warden"),
            EntityType.Builder.create(WardenEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<WarriorHireableEntity> WARRIOR = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "warrior_mercenary"),
            EntityType.Builder.create(WarriorHireableEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<ArcherHireableEntity> ARCHER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "archer_mercenary"),
            EntityType.Builder.create(ArcherHireableEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<BlockShopEntity> BLOCK_SHOP = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "block_shop"),
            EntityType.Builder.create(BlockShopEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<HumanFarmerEntity> HUMAN_FARMER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(TaleOfKingdoms.MODID, "human_farmer"),
            EntityType.Builder.create(HumanFarmerEntity::new, SpawnGroup.MISC).dimensions(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final List<EntityType<? extends ShopEntity>> SHOP_ENTITIES = List.of(EntityTypes.BLACKSMITH, EntityTypes.ITEM_SHOP, EntityTypes.FOODSHOP, EntityTypes.BLOCK_SHOP, EntityTypes.STOCK_MARKET);
}
