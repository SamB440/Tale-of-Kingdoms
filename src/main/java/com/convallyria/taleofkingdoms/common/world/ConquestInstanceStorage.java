package com.convallyria.taleofkingdoms.common.world;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ConquestInstanceStorage {

    private final Map<String, ConquestInstance> conquests = new ConcurrentHashMap<>();
    private String currentWorldName;

    public Optional<ConquestInstance> getConquestInstance(String worldName) {
        return Optional.ofNullable(conquests.get(worldName));
    }

    public void addConquest(String worldName, ConquestInstance instance, boolean current) {
        this.conquests.put(worldName, instance);
        if (current) this.currentWorldName = worldName;
    }

    public void removeConquest(String worldName) {
        conquests.remove(worldName);
    }

    public Optional<ConquestInstance> mostRecentInstance() {
        return Optional.ofNullable(this.currentWorldName != null ? conquests.get(this.currentWorldName) : null);
    }

    public void quit() {
        this.currentWorldName = null;
    }
}
