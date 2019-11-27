package net.islandearth.taleofkingdoms;

import net.islandearth.taleofkingdoms.client.command.TestCommand;
import net.islandearth.taleofkingdoms.common.item.ItemRegistry;
import net.islandearth.taleofkingdoms.common.schematic.Schematic;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Optional;

@Mod(TaleOfKingdoms.MODID)
public class TaleOfKingdoms {
	
    public static final String MODID = "taleofkingdoms";
    public static final String NAME = "Tale of Kingdoms";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger();
    
    private static TaleOfKingdomsAPI api;

    public TaleOfKingdoms() {
        ItemRegistry.init();
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
    	MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    	MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void preInit(FMLCommonSetupEvent event) {
        File file = new File(this.getDataFolder() + "worlds/");
        if (!file.exists()) file.mkdirs();
    	TaleOfKingdoms.api = new TaleOfKingdomsAPI(this);
    	try {
			Schematic.saveAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void serverStarting(FMLServerStartingEvent evt) {
        LOGGER.info("Registering commands...");
        new TestCommand(evt.getCommandDispatcher());
    }
    
    /**
     * Gets the "data folder" of the mod. This is always the modid as a folder in the mods folder.
     * You may get the file using this.
     * @return data folder name
     */
    public String getDataFolder() {
	    return new File(".").getAbsolutePath() + "/mods/" + TaleOfKingdoms.MODID + "/";
    }
    
    /**
     * Gets the API. This will only be present after the mod has finished the {@link FMLCommonSetupEvent}.
     * @return api of {@link TaleOfKingdoms}
     */
	public static Optional<TaleOfKingdomsAPI> getAPI() {
		return Optional.ofNullable(api);
	}
}

