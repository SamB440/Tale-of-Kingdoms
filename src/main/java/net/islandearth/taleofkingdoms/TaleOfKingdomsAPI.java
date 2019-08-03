package net.islandearth.taleofkingdoms;

import net.islandearth.taleofkingdoms.common.world.ConquestInstanceStorage;

public class TaleOfKingdomsAPI {
	
	private TaleOfKingdoms mod;
	private ConquestInstanceStorage cis;
	
	public TaleOfKingdomsAPI(TaleOfKingdoms mod) {
		this.mod = mod;
		this.cis = new ConquestInstanceStorage();
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
}
