package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.common.block.SellBlock;
import com.convallyria.taleofkingdoms.common.block.entity.SellBlockEntity;
import com.convallyria.taleofkingdoms.common.config.TaleOfKingdomsConfig;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
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
import com.convallyria.taleofkingdoms.common.generator.processor.GatewayStructureProcessor;
import com.convallyria.taleofkingdoms.common.generator.processor.GuildStructureProcessor;
import com.convallyria.taleofkingdoms.common.generator.processor.PlayerKingdomStructureProcessor;
import com.convallyria.taleofkingdoms.common.generator.structure.TOKStructures;
import com.convallyria.taleofkingdoms.common.item.ItemRegistry;
import com.convallyria.taleofkingdoms.common.listener.BlockListener;
import com.convallyria.taleofkingdoms.common.listener.CoinListener;
import com.convallyria.taleofkingdoms.common.listener.DeleteWorldListener;
import com.convallyria.taleofkingdoms.common.listener.KingdomListener;
import com.convallyria.taleofkingdoms.common.listener.MobDeathListener;
import com.convallyria.taleofkingdoms.common.listener.MobSpawnListener;
import com.convallyria.taleofkingdoms.common.listener.SleepListener;
import com.convallyria.taleofkingdoms.common.serialization.gson.ConquestInstanceAdapter;
import com.convallyria.taleofkingdoms.common.shop.SellScreenHandler;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.managers.SoundManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Locale;

public class TaleOfKingdoms {

    public static final String MODID = "taleofkingdoms";

    public static int DATA_FORMAT_VERSION = 1;

    public static final Logger LOGGER = LogManager.getLogger();

    // Woo! Static!
    private static TaleOfKingdoms instance;
    private static TaleOfKingdomsAPI api;
    public static TaleOfKingdomsConfig CONFIG;

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    // Deferred registrations (see EntityTypes for the NeoForge freeze rationale).
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS = DeferredRegister.create(MODID, Registries.STRUCTURE_PROCESSOR);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(MODID, Registries.MENU);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MODID, Registries.BLOCK);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(MODID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<StructureProcessorType<GuildStructureProcessor>> GUILD_PROCESSOR = STRUCTURE_PROCESSORS.register("guild", () -> (StructureProcessorType<GuildStructureProcessor>) () -> GuildStructureProcessor.CODEC);
    public static final RegistrySupplier<StructureProcessorType<GatewayStructureProcessor>> GATEWAY_PROCESSOR = STRUCTURE_PROCESSORS.register("gateway", () -> (StructureProcessorType<GatewayStructureProcessor>) () -> GatewayStructureProcessor.CODEC);
    public static final RegistrySupplier<StructureProcessorType<PlayerKingdomStructureProcessor>> KINGDOM_PROCESSOR = STRUCTURE_PROCESSORS.register("kingdom", () -> (StructureProcessorType<PlayerKingdomStructureProcessor>) () -> PlayerKingdomStructureProcessor.CODEC);

    // a public identifier for multiple parts of our bigger chest
    public static final ResourceLocation SELL_BLOCK_IDENTIFIER = ResourceLocation.fromNamespaceAndPath(MODID, "sell_block");

    public static final RegistrySupplier<Block> SELL_BLOCK = BLOCKS.register(SELL_BLOCK_IDENTIFIER, () -> new SellBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST)));
    // The block must be committed before the block-entity supplier resolves it; see init().
    public static final RegistrySupplier<BlockEntityType<SellBlockEntity>> SELL_BLOCK_ENTITY = BLOCK_ENTITIES.register(SELL_BLOCK_IDENTIFIER, () -> BlockEntityType.Builder.of(SellBlockEntity::new, SELL_BLOCK.get()).build(null));
    public static final RegistrySupplier<MenuType<SellScreenHandler>> SELL_SCREEN_HANDLER = MENUS.register("sell_screen_handler", () -> new MenuType<>(SellScreenHandler::new, FeatureFlags.VANILLA_SET));

    public static void setAPI(TaleOfKingdomsAPI api) {
        if (TaleOfKingdoms.api != null) {
            throw new IllegalArgumentException("API already set!");
        }
        TaleOfKingdoms.api = api;
    }

    public static TaleOfKingdoms getInstance() {
        return instance;
    }

    /**
     * Loader-agnostic mod initialisation. Invoked from each loader's entrypoint
     * (Fabric {@code ModInitializer} / NeoForge {@code @Mod} constructor).
     */
    public void init() {
        instance = this;

        // Commit all deferred registrations. Ordering only matters where one registry's supplier
        // resolves another's entry (block entity -> block), so blocks precede block entities.
        EntityTypes.ENTITY_TYPES.register();
        ItemRegistry.init();
        BLOCKS.register();
        BLOCK_ENTITIES.register();
        MENUS.register();
        STRUCTURE_PROCESSORS.register();
        TOKStructures.STRUCTURE_PIECES.register();
        TOKStructures.STRUCTURE_TYPES.register();
        SoundManager.SOUND_EVENTS.register();

        File file = new File(this.getDataFolder() + "worlds");
        if (!file.exists() && !file.mkdirs()) {
            LOGGER.error("Failed to create worlds folder");
        }

        registerEvents();
        registerCommands();

        EntityAttributeRegistry.register(EntityTypes.INNKEEPER, InnkeeperEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.FARMER, FarmerEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.GUILDMASTER, GuildMasterEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.GUILDMASTER_DEFENDER, GuildMasterDefenderEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.BLACKSMITH, BlacksmithEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.CITYBUILDER, CityBuilderEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.KNIGHT, KnightEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.HUNTER, HunterEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.GUILDGUARD, GuildGuardEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.GUILDVILLAGER, GuildVillagerEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.GUILDARCHER, GuildArcherEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.BANKER, BankerEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.LONE, LoneEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.FOODSHOP, FoodShopEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.GUILDCAPTAIN, GuildCaptainEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.LONEVILLAGER, LoneVillagerEntity::createMobAttributes);

        EntityAttributeRegistry.register(EntityTypes.REFICULE_SOLDIER, ReficuleSoldierEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.REFICULE_GUARDIAN, ReficuleGuardianEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.REFICULE_MAGE, ReficuleMageEntity::createMobAttributes);

        EntityAttributeRegistry.register(EntityTypes.BANDIT, BanditEntity::createMobAttributes);

        // Player's kingdom entities
        EntityAttributeRegistry.register(EntityTypes.ITEM_SHOP, ItemShopEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.KINGDOM_VILLAGER, KingdomVillagerEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.STOCK_MARKET, StockMarketEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.QUARRY_FOREMAN, QuarryForemanEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.LUMBER_FOREMAN, LumberForemanEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.QUARRY_WORKER, QuarryWorkerEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.LUMBER_WORKER, LumberWorkerEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.WARDEN, WardenEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.WARRIOR, WarriorHireableEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.ARCHER, ArcherHireableEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.BLOCK_SHOP, BlockShopEntity::createMobAttributes);
        EntityAttributeRegistry.register(EntityTypes.HUMAN_FARMER, HumanFarmerEntity::createAttributes);

        // Load shop items
        new ShopParser().createShopItems();
        ShopParser.SHOP_ITEMS.values().forEach(shopItems -> shopItems.forEach(shopItem -> LOGGER.info("Loaded item value {}", shopItem.toString())));
        CONFIG = AutoConfig.register(TaleOfKingdomsConfig.class, PartitioningSerializer.wrap(Toml4jConfigSerializer::new)).getConfig();
    }

    /**
     * Gets the "data folder" of the mod. This is always the modid as a folder in the mods folder.
     * You may get the file using this.
     * @return data folder name
     */
    @NotNull
    public String getDataFolder() {
        return new File(".").getAbsolutePath() + File.separator + "config" + File.separator + TaleOfKingdoms.MODID + File.separator;
    }

    /**
     * Gets the API. This will only be present after the mod has finished loading.
     * @return api of {@link TaleOfKingdoms}
     */
    public static TaleOfKingdomsAPI getAPI() {
        return api;
    }

    private void registerEvents() {
        TaleOfKingdoms.LOGGER.info("Registering events...");
        new CoinListener();
        new SleepListener();
        new MobSpawnListener();
        new MobDeathListener();
        new BlockListener();
        new KingdomListener();
        new DeleteWorldListener();
    }

    private void registerCommands() {
        new TaleOfKingdomsCommands();
    }

    public static Component parse(StringReader stringReader, HolderLookup.Provider registries) throws CommandSyntaxException {
        try {
            Component text = Component.Serializer.fromJson(stringReader.getString(), registries);
            if (text == null) {
                throw ComponentArgument.ERROR_INVALID_JSON.createWithContext(stringReader, "empty");
            } else {
                return text;
            }
        } catch (JsonParseException var4) {
            String string = var4.getCause() != null ? var4.getCause().getMessage() : var4.getMessage();
            throw ComponentArgument.ERROR_INVALID_JSON.createWithContext(stringReader, string);
        }
    }

    public Gson getGson() {
        return new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(ConquestInstance.class, new ConquestInstanceAdapter())
                .create();
    }
}