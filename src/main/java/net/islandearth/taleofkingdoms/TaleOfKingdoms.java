package net.islandearth.taleofkingdoms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.islandearth.taleofkingdoms.common.entity.EntityTypes;
import net.islandearth.taleofkingdoms.common.entity.generic.HunterEntity;
import net.islandearth.taleofkingdoms.common.entity.generic.KnightEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.FarmerEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import net.islandearth.taleofkingdoms.common.gson.BlockPosAdapter;
import net.islandearth.taleofkingdoms.common.item.ItemRegistry;
import net.islandearth.taleofkingdoms.common.listener.BlockListener;
import net.islandearth.taleofkingdoms.common.listener.CoinListener;
import net.islandearth.taleofkingdoms.common.listener.DeleteWorldListener;
import net.islandearth.taleofkingdoms.common.listener.KingdomListener;
import net.islandearth.taleofkingdoms.common.listener.MobSpawnListener;
import net.islandearth.taleofkingdoms.common.listener.SleepListener;
import net.islandearth.taleofkingdoms.common.schematic.Schematic;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;

public class TaleOfKingdoms implements ModInitializer {

    public static final String MODID = "taleofkingdoms";
    public static final String NAME = "Tale of Kingdoms";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger();

    private static TaleOfKingdomsAPI api;

    @Override
    public void onInitialize() {
        ItemRegistry.init();

        File file = new File(this.getDataFolder() + "worlds/");
        if (!file.exists()) file.mkdirs();
        registerEvents();
        TaleOfKingdoms.api = new TaleOfKingdomsAPI(this);
        try {
            Schematic.saveAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FabricDefaultAttributeRegistry.register(EntityTypes.INNKEEPER, InnkeeperEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.FARMER, FarmerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.GUILDMASTER, GuildMasterEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.BLACKSMITH, BlacksmithEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.CITYBUILDER, CityBuilderEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.KNIGHT, KnightEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.HUNTER, HunterEntity.createMobAttributes());
    }

    /**
     * Gets the "data folder" of the mod. This is always the modid as a folder in the mods folder.
     * You may get the file using this.
     * @return data folder name
     */
    @NotNull
    public String getDataFolder() {
        return new File(".").getAbsolutePath() + "/mods/" + TaleOfKingdoms.MODID + "/";
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
        new BlockListener();
        new KingdomListener();
        new DeleteWorldListener();
    }

    public Gson getGson() {
        return new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(BlockPos.class, new BlockPosAdapter())
                .create();
    }
}

