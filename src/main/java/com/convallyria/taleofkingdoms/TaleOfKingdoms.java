package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.gui.shop.SellScreenHandler;
import com.convallyria.taleofkingdoms.common.block.SellBlock;
import com.convallyria.taleofkingdoms.common.block.entity.SellBlockEntity;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
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
import com.convallyria.taleofkingdoms.common.generator.GatewayGenerator;
import com.convallyria.taleofkingdoms.common.generator.ReficuleVillageGenerator;
import com.convallyria.taleofkingdoms.common.generator.feature.GatewayFeature;
import com.convallyria.taleofkingdoms.common.generator.feature.ReficuleVillageFeature;
import com.convallyria.taleofkingdoms.common.generator.processor.GatewayStructureProcessor;
import com.convallyria.taleofkingdoms.common.generator.processor.GuildStructureProcessor;
import com.convallyria.taleofkingdoms.common.gson.BlockPosAdapter;
import com.convallyria.taleofkingdoms.common.item.ItemRegistry;
import com.convallyria.taleofkingdoms.common.listener.BlockListener;
import com.convallyria.taleofkingdoms.common.listener.CoinListener;
import com.convallyria.taleofkingdoms.common.listener.DeleteWorldListener;
import com.convallyria.taleofkingdoms.common.listener.KingdomListener;
import com.convallyria.taleofkingdoms.common.listener.MobDeathListener;
import com.convallyria.taleofkingdoms.common.listener.MobSpawnListener;
import com.convallyria.taleofkingdoms.common.listener.SleepListener;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Mod("taleofkingdoms")
public class TaleOfKingdoms {

    public static final String MODID = "taleofkingdoms";
    public static final String NAME = "Tale of Kingdoms";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger();

    private static TaleOfKingdomsAPI api;

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    public static final ResourceLocation INSTANCE_PACKET_ID = new ResourceLocation(TaleOfKingdoms.MODID, "instance");
    public static final ResourceLocation SIGN_CONTRACT_PACKET_ID = new ResourceLocation(TaleOfKingdoms.MODID, "sign_contract");
    public static final ResourceLocation FIX_GUILD_PACKET_ID = new ResourceLocation(TaleOfKingdoms.MODID, "fix_guild");
    public static final ResourceLocation TOGGLE_SELL_GUI_PACKET_ID = new ResourceLocation(TaleOfKingdoms.MODID, "open_sell_gui");
    public static final ResourceLocation BUY_ITEM_PACKET_ID = new ResourceLocation(TaleOfKingdoms.MODID, "buy_item");
    public static final ResourceLocation BANKER_INTERACT_PACKET_ID = new ResourceLocation(TaleOfKingdoms.MODID, "banker_interact");
    public static final ResourceLocation HUNTER_PACKET_ID = new ResourceLocation(TaleOfKingdoms.MODID, "hunter");
    public static final ResourceLocation INNKEEPER_PACKET_ID = new ResourceLocation(TaleOfKingdoms.MODID, "innkeeper");

    public static final StructurePieceType REFICULE_VILLAGE = ReficuleVillageGenerator.ReficuleVillagePiece::new;
    public static final StructureFeature<NoneFeatureConfiguration> REFICULE_VILLAGE_STRUCTURE = new ReficuleVillageFeature(NoneFeatureConfiguration.CODEC);
    private static final ConfiguredStructureFeature<?, ?> REFICULE_VILLAGE_CONFIGURED = REFICULE_VILLAGE_STRUCTURE.configured(NoneFeatureConfiguration.NONE);
    public static final StructurePieceType GATEWAY = GatewayGenerator.GatewayPiece::new;
    private static final StructureFeature<NoneFeatureConfiguration> GATEWAY_STRUCTURE = new GatewayFeature(NoneFeatureConfiguration.CODEC);
    private static final ConfiguredStructureFeature<?, ?> GATEWAY_CONFIGURED = GATEWAY_STRUCTURE.configured(NoneFeatureConfiguration.NONE);

    public static final StructureProcessorType<?> GUILD_PROCESSOR = StructureProcessorType.register("guild", GuildStructureProcessor.CODEC);
    public static final StructureProcessorType<?> GATEWAY_PROCESSOR = StructureProcessorType.register("gateway", GatewayStructureProcessor.CODEC);

    public static final MenuType<SellScreenHandler> SELL_SCREEN_HANDLER;

    public static final Block SELL_BLOCK;
    public static final BlockEntityType<SellBlockEntity> SELL_BLOCK_ENTITY;

    // a public identifier for multiple parts of our bigger chest
    public static final ResourceLocation SELL_BLOCK_IDENTIFIER = new ResourceLocation(MODID, "sell_block");

    static {
        //We use registerSimple here because our Entity is not an ExtendedScreenHandlerFactory
        //but a NamedScreenHandlerFactory.
        //In a later Tutorial you will see what ExtendedScreenHandlerFactory can do!
        SELL_SCREEN_HANDLER = IForgeContainerType.create((windowId, inv, data) -> new SellScreenHandler(windowId, inv));
        ForgeRegistries.CONTAINERS.register(SELL_SCREEN_HANDLER);
        //SELL_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new ResourceLocation(TaleOfKingdoms.MODID, "sell_screen"), SellScreenHandler::new);

        SELL_BLOCK = Registry.register(Registry.BLOCK, SELL_BLOCK_IDENTIFIER, new SellBlock(BlockBehaviour.Properties.copy(Blocks.CHEST)));

        //The parameter of build at the very end is always null, do not worry about it
        SELL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, SELL_BLOCK_IDENTIFIER, BlockEntityType.Builder.of(SellBlockEntity::new, SELL_BLOCK).build(null));
    }

    public TaleOfKingdoms() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ItemRegistry.init();

        File file = new File(this.getDataFolder() + "worlds");
        if (!file.exists()) file.mkdirs();
        registerEvents();
        registerCommands();
        TaleOfKingdoms.api = new TaleOfKingdomsAPI(this);

        this.registerFeatures();

        // Load shop items
        new ShopParser().createShopItems();
        ShopParser.guiShopItems.values().forEach(shopItems -> shopItems.forEach(shopItem -> LOGGER.info("Loaded item value " + shopItem.toString())));
    }

    @OnlyIn(Dist.CLIENT)
    private void setupClient(final FMLClientSetupEvent event) {
        new TaleOfKingdomsClient().onInitializeClient();
    }

    @SubscribeEvent
    public void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityTypes.INNKEEPER, InnkeeperEntity.createMobAttributes().build());
        event.put(EntityTypes.FARMER, FarmerEntity.createMobAttributes().build());
        event.put(EntityTypes.GUILDMASTER, GuildMasterEntity.createMobAttributes().build());
        event.put(EntityTypes.GUILDMASTER_DEFENDER, GuildMasterDefenderEntity.createMobAttributes().build());
        event.put(EntityTypes.BLACKSMITH, BlacksmithEntity.createMobAttributes().build());
        event.put(EntityTypes.CITYBUILDER, CityBuilderEntity.createMobAttributes().build());
        event.put(EntityTypes.KNIGHT, KnightEntity.createMobAttributes().build());
        event.put(EntityTypes.HUNTER, HunterEntity.createMobAttributes().build());
        event.put(EntityTypes.GUILDGUARD, GuildGuardEntity.createMobAttributes().build());
        event.put(EntityTypes.GUILDARCHER, GuildArcherEntity.createMobAttributes().build());
        event.put(EntityTypes.BANKER, BankerEntity.createMobAttributes().build());
        event.put(EntityTypes.LONE, LoneEntity.createMobAttributes().build());
        event.put(EntityTypes.FOODSHOP, FoodShopEntity.createMobAttributes().build());
        event.put(EntityTypes.GUILDCAPTAIN, GuildCaptainEntity.createMobAttributes().build());
        event.put(EntityTypes.LONEVILLAGER, LoneVillagerEntity.createMobAttributes().build());
        event.put(EntityTypes.REFICULE_SOLDIER, ReficuleSoldierEntity.createMobAttributes().build());
        event.put(EntityTypes.REFICULE_GUARDIAN, ReficuleGuardianEntity.createMobAttributes().build());
        event.put(EntityTypes.REFICULE_MAGE, ReficuleMageEntity.createMobAttributes().build());
    }

    /**
     * Gets the "data folder" of the mod. This is always the modid as a folder in the mods folder.
     * You may get the file using this.
     * @return data folder name
     */
    @NotNull
    public String getDataFolder() {
        return new File(".").getAbsolutePath() + File.separator + "mods" + File.separator + TaleOfKingdoms.MODID + File.separator;
    }

    /**
     * Gets the API. This will only be present after the mod has finished loading.
     * @return api of {@link TaleOfKingdoms}
     */
    public static Optional<TaleOfKingdomsAPI> getAPI() {
        return Optional.ofNullable(api);
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

    private void registerFeatures() {
        //Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MODID, "reficule_village_piece"), REFICULE_VILLAGE);
        Random random = ThreadLocalRandom.current();
        int seed = random.nextInt(9000) + 1000;
        /*
        FabricStructureBuilder.create(new ResourceLocation(MODID, "reficule_village"), REFICULE_VILLAGE_STRUCTURE)
                .step(GenerationStep.Decoration.SURFACE_STRUCTURES)
                .defaultConfig(48, 8, seed)
                .adjustsSurface()
                .register();

        ResourceKey<ConfiguredStructureFeature<?, ?>> reficuleVillage = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
                new ResourceLocation(MODID, "reficule_village"));
        BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, reficuleVillage.location(), REFICULE_VILLAGE_CONFIGURED);
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.BiomeCategory.PLAINS, Biome.BiomeCategory.FOREST,
                Biome.BiomeCategory.JUNGLE, Biome.BiomeCategory.ICY, Biome.BiomeCategory.TAIGA, Biome.BiomeCategory.SAVANNA, Biome.BiomeCategory.MESA), reficuleVillage);

        Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MODID, "gateway_piece"), GATEWAY);
        FabricStructureBuilder.create(new ResourceLocation(MODID, "gateway"), GATEWAY_STRUCTURE)
                .step(GenerationStep.Decoration.SURFACE_STRUCTURES)
                .defaultConfig(16, 8, seed - 256)
                .register();

        ResourceKey<ConfiguredStructureFeature<?, ?>> gateway = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
                new ResourceLocation(MODID, "gateway"));
        BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, gateway.location(), GATEWAY_CONFIGURED);
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.BiomeCategory.PLAINS, Biome.BiomeCategory.FOREST,
                Biome.BiomeCategory.JUNGLE, Biome.BiomeCategory.ICY, Biome.BiomeCategory.DESERT, Biome.BiomeCategory.MESA), gateway);*/
    }

    public static Component parse(StringReader stringReader) throws CommandSyntaxException {
        try {
            Component text = Component.Serializer.fromJson(stringReader);
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
                .registerTypeAdapter(BlockPos.class, new BlockPosAdapter())
                .create();
    }
}