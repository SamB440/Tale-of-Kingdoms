package net.islandearth.taleofkingdoms;

import net.islandearth.taleofkingdoms.common.world.ConquestInstanceStorage;
import net.islandearth.taleofkingdoms.managers.IManager;
import net.islandearth.taleofkingdoms.managers.SoundManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

	@Nonnull
    public ConquestInstanceStorage getConquestInstanceStorage() {
    	return cis;
    }

	/**
	 * Gets the "data folder" of the mod. This is always the modid as a folder in the mods folder.
	 * You may get the file using this.
	 * @return data folder name
	 */
	@Nonnull
	public String getDataFolder() {
	    return mod.getDataFolder();
    }

    @Nonnull
    public TaleOfKingdoms getMod() {
    	return mod;
    }

	/**
	 * Gets a manager by its name.
	 * @param name name of the {@link IManager}
	 * @return the {@link IManager} or null if not found
	 */
	@Nullable
	public IManager getManager(String name) {
    	return managers.get(name);
    }

    @Nonnull
    public Set<String> getManagers() {
    	return managers.keySet();
    }
}
