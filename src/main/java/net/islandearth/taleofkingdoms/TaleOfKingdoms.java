package net.islandearth.taleofkingdoms;

import java.io.File;
import java.util.Optional;

import org.apache.logging.log4j.Logger;

import net.islandearth.taleofkingdoms.client.command.TestCommand;
import net.islandearth.taleofkingdoms.client.gui.RenderListener;
import net.islandearth.taleofkingdoms.common.item.ItemRegistry;
import net.islandearth.taleofkingdoms.common.listener.CoinListener;
import net.islandearth.taleofkingdoms.common.listener.StartWorldListener;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;

@Mod(modid = TaleOfKingdoms.MODID, name = TaleOfKingdoms.NAME, version = TaleOfKingdoms.VERSION, acceptedMinecraftVersions = "1.12.2")
public class TaleOfKingdoms {
	
    public static final String MODID = "taleofkingdoms";
    public static final String NAME = "Tale of Kingdoms";
    public static final String VERSION = "1.0.0";

    public static Logger logger;
    
    @Mod.Instance
    private TaleOfKingdoms instance;
    
    private static TaleOfKingdomsAPI api;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.instance = this;
        logger = event.getModLog();
        File file = new File(this.getDataFolder() + "worlds/");
        if (!file.exists()) file.mkdirs();
        ItemRegistry.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    	registerEvents();
    	registerCommands();
    	TaleOfKingdoms.api = new TaleOfKingdomsAPI(this);
    }
    
    private void registerEvents() {
    	EventBus bus = MinecraftForge.EVENT_BUS;
    	bus.register(new StartWorldListener());
    	bus.register(new RenderListener());
    	bus.register(new CoinListener());
    }
    
    private void registerCommands() {
    	ClientCommandHandler.instance.registerCommand(new TestCommand());
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

