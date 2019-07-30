package net.islandearth.taleofkingdoms;

import org.apache.logging.log4j.Logger;

import net.islandearth.taleofkingdoms.client.listener.StartWorldListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;

//TODO make this compatible with server AND client !
@Mod(modid = TaleOfKingdoms.MODID, name = TaleOfKingdoms.NAME, version = TaleOfKingdoms.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "1.12.2")
public class TaleOfKingdoms {
	
    public static final String MODID = "taleofkingdoms";
    public static final String NAME = "Tale of Kingdoms";
    public static final String VERSION = "1.0.0";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    	registerEvents();
    }
    
    private void registerEvents() {
    	EventBus bus = MinecraftForge.EVENT_BUS;
    	bus.register(new StartWorldListener());
    }
}

