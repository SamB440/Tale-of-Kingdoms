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
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntityTypes {

    private static final float HUMAN_WIDTH = 0.6f;
    private static final float HUMAN_HEIGHT = 1.8f;

    /**
     * Deferred entity registrations. Registration is deferred rather than eager because NeoForge
     * freezes {@code BuiltInRegistries} before mods load; Architectury commits this to each loader
     * at the correct lifecycle point when {@link #ENTITY_TYPES}.register() is called from init.
     */
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(TaleOfKingdoms.MODID, Registries.ENTITY_TYPE);

    private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String id, EntityType.Builder<T> builder) {
        // Must use the vanilla build(String) overload — the no-arg build() is a Fabric API
        // interface-injected method (FabricEntityType$Builder) and throws ClassNotFoundException
        // at runtime on NeoForge. The key is only used for datafixer lookup.
        return ENTITY_TYPES.register(id, () -> builder.build(id));
    }

    private static <T extends Entity> EntityType.Builder<T> human(EntityType.EntityFactory<T> factory) {
        return EntityType.Builder.of(factory, MobCategory.MISC).sized(HUMAN_WIDTH, HUMAN_HEIGHT);
    }

    public static final RegistrySupplier<EntityType<FarmerEntity>> FARMER = register("farmer", human(FarmerEntity::new));
    public static final RegistrySupplier<EntityType<GuildMasterEntity>> GUILDMASTER = register("guild_master", human(GuildMasterEntity::new));
    public static final RegistrySupplier<EntityType<GuildMasterDefenderEntity>> GUILDMASTER_DEFENDER = register("guild_master_defender", human(GuildMasterDefenderEntity::new).fireImmune());
    public static final RegistrySupplier<EntityType<BlacksmithEntity>> BLACKSMITH = register("blacksmith", human(BlacksmithEntity::new));
    public static final RegistrySupplier<EntityType<CityBuilderEntity>> CITYBUILDER = register("city_builder", human(CityBuilderEntity::new));
    public static final RegistrySupplier<EntityType<KnightEntity>> KNIGHT = register("knight", human(KnightEntity::new));
    public static final RegistrySupplier<EntityType<InnkeeperEntity>> INNKEEPER = register("innkeeper", human(InnkeeperEntity::new));
    public static final RegistrySupplier<EntityType<HunterEntity>> HUNTER = register("hunter", human(HunterEntity::new));
    public static final RegistrySupplier<EntityType<GuildGuardEntity>> GUILDGUARD = register("guild_guard", human(GuildGuardEntity::new));
    public static final RegistrySupplier<EntityType<GuildVillagerEntity>> GUILDVILLAGER = register("guild_villager", human(GuildVillagerEntity::new));
    public static final RegistrySupplier<EntityType<GuildArcherEntity>> GUILDARCHER = register("guild_archer", human(GuildArcherEntity::new));
    public static final RegistrySupplier<EntityType<LoneEntity>> LONE = register("lone", human(LoneEntity::new));
    public static final RegistrySupplier<EntityType<BankerEntity>> BANKER = register("banker", human(BankerEntity::new));
    public static final RegistrySupplier<EntityType<FoodShopEntity>> FOODSHOP = register("foodshop", human(FoodShopEntity::new));
    public static final RegistrySupplier<EntityType<GuildCaptainEntity>> GUILDCAPTAIN = register("guild_captain", human(GuildCaptainEntity::new));
    public static final RegistrySupplier<EntityType<LoneVillagerEntity>> LONEVILLAGER = register("lone_villager", human(LoneVillagerEntity::new));

    public static final RegistrySupplier<EntityType<ReficuleSoldierEntity>> REFICULE_SOLDIER = register("reficule_soldier", human(ReficuleSoldierEntity::new));
    public static final RegistrySupplier<EntityType<ReficuleGuardianEntity>> REFICULE_GUARDIAN = register("reficule_guardian", human(ReficuleGuardianEntity::new));
    public static final RegistrySupplier<EntityType<ReficuleMageEntity>> REFICULE_MAGE = register("reficule_mage", human(ReficuleMageEntity::new));

    public static final RegistrySupplier<EntityType<BanditEntity>> BANDIT = register("bandit", human(BanditEntity::new));

    // =========================
    // Player's kingdom entities
    // =========================
    public static final RegistrySupplier<EntityType<ItemShopEntity>> ITEM_SHOP = register("item_shop", human(ItemShopEntity::new));
    public static final RegistrySupplier<EntityType<KingdomVillagerEntity>> KINGDOM_VILLAGER = register("kingdom_villager", human(KingdomVillagerEntity::new));
    public static final RegistrySupplier<EntityType<StockMarketEntity>> STOCK_MARKET = register("stock_market", human(StockMarketEntity::new));
    public static final RegistrySupplier<EntityType<QuarryForemanEntity>> QUARRY_FOREMAN = register("quarry_foreman", human(QuarryForemanEntity::new));
    public static final RegistrySupplier<EntityType<LumberForemanEntity>> LUMBER_FOREMAN = register("lumber_foreman", human(LumberForemanEntity::new));
    public static final RegistrySupplier<EntityType<QuarryWorkerEntity>> QUARRY_WORKER = register("quarry_worker", human(QuarryWorkerEntity::new));
    public static final RegistrySupplier<EntityType<LumberWorkerEntity>> LUMBER_WORKER = register("lumber_worker", human(LumberWorkerEntity::new));
    public static final RegistrySupplier<EntityType<WardenEntity>> WARDEN = register("warden", human(WardenEntity::new));
    public static final RegistrySupplier<EntityType<WarriorHireableEntity>> WARRIOR = register("warrior_mercenary", human(WarriorHireableEntity::new));
    public static final RegistrySupplier<EntityType<ArcherHireableEntity>> ARCHER = register("archer_mercenary", human(ArcherHireableEntity::new));
    public static final RegistrySupplier<EntityType<BlockShopEntity>> BLOCK_SHOP = register("block_shop", human(BlockShopEntity::new));
    public static final RegistrySupplier<EntityType<HumanFarmerEntity>> HUMAN_FARMER = register("human_farmer", human(HumanFarmerEntity::new));

    public static final List<RegistrySupplier<? extends EntityType<? extends ShopEntity>>> SHOP_ENTITIES = List.of(BLACKSMITH, ITEM_SHOP, FOODSHOP, BLOCK_SHOP, STOCK_MARKET);
}
