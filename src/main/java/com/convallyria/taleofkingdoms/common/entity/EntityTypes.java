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
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityTypes {

    private static final float HUMAN_WIDTH = 0.6f;
    private static final float HUMAN_HEIGHT = 1.8f;

    public static final EntityType<FarmerEntity> FARMER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "farmer"),
            EntityType.Builder.of(FarmerEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildMasterEntity> GUILDMASTER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "guild_master"),
            EntityType.Builder.of(GuildMasterEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildMasterDefenderEntity> GUILDMASTER_DEFENDER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "guild_master_defender"),
            EntityType.Builder.of(GuildMasterDefenderEntity::new, MobCategory.MISC).fireImmune().sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<BlacksmithEntity> BLACKSMITH = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "blacksmith"),
            EntityType.Builder.of(BlacksmithEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<CityBuilderEntity> CITYBUILDER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "city_builder"),
            EntityType.Builder.of(CityBuilderEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<KnightEntity> KNIGHT = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "knight"),
            EntityType.Builder.of(KnightEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<InnkeeperEntity> INNKEEPER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "innkeeper"),
            EntityType.Builder.of(InnkeeperEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<HunterEntity> HUNTER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "hunter"),
            EntityType.Builder.of(HunterEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildGuardEntity> GUILDGUARD = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "guild_guard"),
            EntityType.Builder.of(GuildGuardEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildVillagerEntity> GUILDVILLAGER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "guild_villager"),
            EntityType.Builder.of(GuildVillagerEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildArcherEntity> GUILDARCHER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "guild_archer"),
            EntityType.Builder.of(GuildArcherEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<LoneEntity> LONE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "lone"),
            EntityType.Builder.of(LoneEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<BankerEntity> BANKER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "banker"),
            EntityType.Builder.of(BankerEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<FoodShopEntity> FOODSHOP = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "foodshop"),
            EntityType.Builder.of(FoodShopEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<GuildCaptainEntity> GUILDCAPTAIN = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "guild_captain"),
            EntityType.Builder.of(GuildCaptainEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<LoneVillagerEntity> LONEVILLAGER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "lone_villager"),
            EntityType.Builder.of(LoneVillagerEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<ReficuleSoldierEntity> REFICULE_SOLDIER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "reficule_soldier"),
            EntityType.Builder.of(ReficuleSoldierEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<ReficuleGuardianEntity> REFICULE_GUARDIAN = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "reficule_guardian"),
            EntityType.Builder.of(ReficuleGuardianEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );
    public static final EntityType<ReficuleMageEntity> REFICULE_MAGE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "reficule_mage"),
            EntityType.Builder.of(ReficuleMageEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<BanditEntity> BANDIT = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "bandit"),
            EntityType.Builder.of(BanditEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    // =========================
    // Player's kingdom entities
    // =========================
    public static final EntityType<ItemShopEntity> ITEM_SHOP = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "item_shop"),
            EntityType.Builder.of(ItemShopEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<KingdomVillagerEntity> KINGDOM_VILLAGER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "kingdom_villager"),
            EntityType.Builder.of(KingdomVillagerEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<StockMarketEntity> STOCK_MARKET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "stock_market"),
            EntityType.Builder.of(StockMarketEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<QuarryForemanEntity> QUARRY_FOREMAN = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "quarry_foreman"),
            EntityType.Builder.of(QuarryForemanEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<LumberForemanEntity> LUMBER_FOREMAN = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "lumber_foreman"),
            EntityType.Builder.of(LumberForemanEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<QuarryWorkerEntity> QUARRY_WORKER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "quarry_worker"),
            EntityType.Builder.of(QuarryWorkerEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<LumberWorkerEntity> LUMBER_WORKER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "lumber_worker"),
            EntityType.Builder.of(LumberWorkerEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<WardenEntity> WARDEN = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "warden"),
            EntityType.Builder.of(WardenEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<WarriorHireableEntity> WARRIOR = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "warrior_mercenary"),
            EntityType.Builder.of(WarriorHireableEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<ArcherHireableEntity> ARCHER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "archer_mercenary"),
            EntityType.Builder.of(ArcherHireableEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<BlockShopEntity> BLOCK_SHOP = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "block_shop"),
            EntityType.Builder.of(BlockShopEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final EntityType<HumanFarmerEntity> HUMAN_FARMER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "human_farmer"),
            EntityType.Builder.of(HumanFarmerEntity::new, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT).build()
    );

    public static final List<EntityType<? extends ShopEntity>> SHOP_ENTITIES = List.of(EntityTypes.BLACKSMITH, EntityTypes.ITEM_SHOP, EntityTypes.FOODSHOP, EntityTypes.BLOCK_SHOP, EntityTypes.STOCK_MARKET);
}
