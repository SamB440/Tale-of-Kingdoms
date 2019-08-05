package net.islandearth.taleofkingdoms;

import java.io.File;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.islandearth.taleofkingdoms.client.command.TestCommand;
import net.islandearth.taleofkingdoms.client.gui.RenderListener;
import net.islandearth.taleofkingdoms.common.item.ItemRegistry;
import net.islandearth.taleofkingdoms.common.listener.CoinListener;
import net.islandearth.taleofkingdoms.common.listener.StartWorldListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
    
    public void preInit(FMLCommonSetupEvent event) {
        File file = new File(this.getDataFolder() + "worlds/");
        if (!file.exists()) file.mkdirs();
    	registerEvents();
    	TaleOfKingdoms.api = new TaleOfKingdomsAPI(this);
    }
    
    public void serverStarting(FMLServerStartingEvent evt) {
        LOGGER.info("Registering commands...");
        new TestCommand(evt.getCommandDispatcher());
    }
    
    private void registerEvents() {
    	IEventBus bus = MinecraftForge.EVENT_BUS;
    	bus.register(new StartWorldListener());
    	bus.register(new RenderListener());
    	bus.register(new CoinListener());
    }
    
    public String getDataFolder() {
	    return new File(".").getAbsolutePath().toString() + "/mods/" + TaleOfKingdoms.MODID + "/";
    }
    
    /**
     * Gets the API. This will only be present after the mod has finished the {@link FMLInitializationEvent}.
     * @return api of {@link TaleOfKingdoms}
     */
	public static Optional<TaleOfKingdomsAPI> getAPI() {
		return Optional.ofNullable(api);
	}
}

