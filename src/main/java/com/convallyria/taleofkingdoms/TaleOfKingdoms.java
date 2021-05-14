package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.client.gui.shop.SellScreenHandler;
import com.convallyria.taleofkingdoms.common.block.SellBlock;
import com.convallyria.taleofkingdoms.common.block.entity.SellBlockEntity;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.KnightEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.LoneVillagerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.*;
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
import com.convallyria.taleofkingdoms.common.listener.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TaleOfKingdoms implements ModInitializer {

    public static final String MODID = "taleofkingdoms";
    public static final String NAME = "Tale of Kingdoms";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger();

    private static TaleOfKingdomsAPI api;

    public static final Identifier INSTANCE_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "instance");
    public static final Identifier SIGN_CONTRACT_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "sign_contract");

    public static final StructurePieceType REFICULE_VILLAGE = ReficuleVillageGenerator.ReficuleVillagePiece::new;
    public static final StructureFeature<DefaultFeatureConfig> REFICULE_VILLAGE_STRUCTURE = new ReficuleVillageFeature(DefaultFeatureConfig.CODEC);
    private static final ConfiguredStructureFeature<?, ?> REFICULE_VILLAGE_CONFIGURED = REFICULE_VILLAGE_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT);
    public static final StructurePieceType GATEWAY = GatewayGenerator.GatewayPiece::new;
    private static final StructureFeature<DefaultFeatureConfig> GATEWAY_STRUCTURE = new GatewayFeature(DefaultFeatureConfig.CODEC);
    private static final ConfiguredStructureFeature<?, ?> GATEWAY_CONFIGURED = GATEWAY_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT);

    public static final StructureProcessorType<?> GUILD_PROCESSOR = StructureProcessorType.register("guild", GuildStructureProcessor.CODEC);
    public static final StructureProcessorType<?> GATEWAY_PROCESSOR = StructureProcessorType.register("gateway", GatewayStructureProcessor.CODEC);

    public static final ScreenHandlerType<SellScreenHandler> SELL_SCREEN_HANDLER;

    public static final Block SELL_BLOCK;
    public static final BlockEntityType<SellBlockEntity> SELL_BLOCK_ENTITY;

    // a public identifier for multiple parts of our bigger chest
    public static final Identifier SELL_BLOCK_IDENTIFIER = new Identifier(MODID, "sell_block");

    static {
        //We use registerSimple here because our Entity is not an ExtendedScreenHandlerFactory
        //but a NamedScreenHandlerFactory.
        //In a later Tutorial you will see what ExtendedScreenHandlerFactory can do!
        SELL_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(TaleOfKingdoms.MODID, "sell_screen"), SellScreenHandler::new);

        SELL_BLOCK = Registry.register(Registry.BLOCK, SELL_BLOCK_IDENTIFIER, new SellBlock(FabricBlockSettings.copyOf(Blocks.CHEST)));

        //The parameter of build at the very end is always null, do not worry about it
        SELL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, SELL_BLOCK_IDENTIFIER, BlockEntityType.Builder.create(SellBlockEntity::new, SELL_BLOCK).build(null));
    }

    private Map<String, Integer> itemValues;

    public Map<String, Integer> getItemValues() {
        return itemValues;
    }

    @Override
    public void onInitialize() {
        ItemRegistry.init();

        File file = new File(this.getDataFolder() + "worlds");
        if (!file.exists()) file.mkdirs();
        registerEvents();
        registerCommands();
        TaleOfKingdoms.api = new TaleOfKingdomsAPI(this);

        this.registerFeatures();

        FabricDefaultAttributeRegistry.register(EntityTypes.INNKEEPER, InnkeeperEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.FARMER, FarmerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.GUILDMASTER, GuildMasterEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.GUILDMASTERCLONE, GuildMasterCloneEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.BLACKSMITH, BlacksmithEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.CITYBUILDER, CityBuilderEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.KNIGHT, KnightEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.HUNTER, HunterEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.GUILDGUARD, GuildGuardEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.GUILDARCHER, GuildArcherEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.BANKER, BankerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.LONE, LoneEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.FOODSHOP, FoodShopEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.GUILDCAPTAIN, GuildCaptainEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.LONEVILLAGER, LoneVillagerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.REFICULE_SOLDIER, ReficuleSoldierEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.REFICULE_GUARDIAN, ReficuleGuardianEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.REFICULE_MAGE, ReficuleMageEntity.createMobAttributes());

        // Load item values
        InputStream inputStream = TaleOfKingdoms.class.getResourceAsStream("/assets/shop/values.json");
        if (inputStream == null) {
            LOGGER.warn("Cannot read shop values file! inputStream is null.");
            return;
        }
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        Type type = new TypeToken<Map<String, Integer>>(){}.getType();
        Map<String, Integer> mapValue = getGson().fromJson(reader, type);
        mapValue.forEach((name, value) -> LOGGER.info("Loaded item value " + name + " (" + value + ")."));
        this.itemValues = mapValue;
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
        Registry.register(Registry.STRUCTURE_PIECE, new Identifier(MODID, "reficule_village_piece"), REFICULE_VILLAGE);
        Random random = ThreadLocalRandom.current();
        int seed = random.nextInt(9000) + 1000;
        FabricStructureBuilder.create(new Identifier(MODID, "reficule_village"), REFICULE_VILLAGE_STRUCTURE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(48, 8, seed)
                .adjustsSurface()
                .register();

        RegistryKey<ConfiguredStructureFeature<?, ?>> reficuleVillage = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN,
                new Identifier(MODID, "reficule_village"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, reficuleVillage.getValue(), REFICULE_VILLAGE_CONFIGURED);
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.PLAINS, Biome.Category.FOREST,
                Biome.Category.JUNGLE, Biome.Category.ICY, Biome.Category.TAIGA, Biome.Category.SAVANNA, Biome.Category.MESA), reficuleVillage);

        Registry.register(Registry.STRUCTURE_PIECE, new Identifier(MODID, "gateway_piece"), GATEWAY);
        FabricStructureBuilder.create(new Identifier(MODID, "gateway"), GATEWAY_STRUCTURE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(16, 8, seed - 256)
                .register();

        RegistryKey<ConfiguredStructureFeature<?, ?>> gateway = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN,
                new Identifier(MODID, "gateway"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, gateway.getValue(), GATEWAY_CONFIGURED);
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.PLAINS, Biome.Category.FOREST,
                Biome.Category.JUNGLE, Biome.Category.ICY, Biome.Category.DESERT, Biome.Category.MESA), gateway);
    }

    public static Text parse(StringReader stringReader) throws CommandSyntaxException {
        try {
            Text text = Text.Serializer.fromJson(stringReader);
            if (text == null) {
                throw TextArgumentType.INVALID_COMPONENT_EXCEPTION.createWithContext(stringReader, "empty");
            } else {
                return text;
            }
        } catch (JsonParseException var4) {
            String string = var4.getCause() != null ? var4.getCause().getMessage() : var4.getMessage();
            throw TextArgumentType.INVALID_COMPONENT_EXCEPTION.createWithContext(stringReader, string);
        }
    }

    public Gson getGson() {
        return new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(BlockPos.class, new BlockPosAdapter())
                .create();
    }
}