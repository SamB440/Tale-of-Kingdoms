package net.islandearth.taleofkingdoms;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.islandearth.taleofkingdoms.common.world.ConquestInstanceStorage;
import net.islandearth.taleofkingdoms.managers.IManager;
import net.islandearth.taleofkingdoms.managers.SoundManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class TaleOfKingdomsAPI {
	
	private final TaleOfKingdoms mod;
	private final ConquestInstanceStorage cis;
	private final Map<String, IManager> managers = new HashMap<>();
	
	public TaleOfKingdomsAPI(TaleOfKingdoms mod) {
		this.mod = mod;
		this.cis = new ConquestInstanceStorage();
		SoundManager sm = new SoundManager(mod);
		managers.put(sm.getName(), sm);
		
		IEventBus bus = MinecraftForge.EVENT_BUS;
		bus.register(sm);
	}
	
    public ConquestInstanceStorage getConquestInstanceStorage() {
    	return cis;
    }
    
    public String getDataFolder() {
	    return mod.getDataFolder();
    }
    
    public TaleOfKingdoms getMod() {
    	return mod;
    }
    
    public IManager getManager(String name) {
    	return managers.get(name);
    }
    
    public Set<String> getManagers() {
    	return managers.keySet();
    }
}
