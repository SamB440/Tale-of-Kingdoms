package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.scheduler.Scheduler;
import com.convallyria.taleofkingdoms.common.schematic.CommonSchematicHandler;
import com.convallyria.taleofkingdoms.common.schematic.SchematicHandler;
import com.convallyria.taleofkingdoms.common.world.ConquestInstanceStorage;
import com.convallyria.taleofkingdoms.managers.IManager;
import com.convallyria.taleofkingdoms.managers.SoundManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class TaleOfKingdomsAPI {

    private final TaleOfKingdoms mod;
    private final ConquestInstanceStorage cis;
    private final Map<Class<? extends IManager>, IManager> managers = new HashMap<>();

    private final Scheduler scheduler;

    public TaleOfKingdomsAPI(TaleOfKingdoms mod) {
        this.mod = mod;
        this.cis = new ConquestInstanceStorage();
        SoundManager sm = new SoundManager(mod);
        managers.put(SoundManager.class, sm);
        this.scheduler = new Scheduler();
    }

    public EnvType getEnvironment() {
        return FabricLoader.getInstance().getEnvironmentType();
    }

    @NotNull
    public Scheduler getScheduler() {
        return scheduler;
    }

    @NotNull
    public ConquestInstanceStorage getConquestInstanceStorage() {
        return cis;
    }

    /**
     * Gets the "data folder" of the mod. This is always the modid as a folder in the mods folder.
     * You may get the file using this.
     * @return data folder name
     */
    @NotNull
    public String getDataFolder() {
        return mod.getDataFolder();
    }

    @NotNull
    public TaleOfKingdoms getMod() {
        return mod;
    }

    public <T extends IManager> T getManager(Class<? extends T> clazz) {
        return (T) managers.getOrDefault(clazz, null);
    }

    @NotNull
    public Collection<IManager> getManagers() {
        return managers.values();
    }

    /**
     * Executes the specified consumer on the correct server environment (either a dedicated server or integrated)
     * @param runnable the consumer to execute, containing the server used
     */
    public abstract void executeOnServerEnvironment(Consumer<MinecraftServer> runnable);

    /**
     * Executes the specified runnable on the main thread of the current environment.
     * @param runnable the runnable to execute
     */
    public abstract void executeOnMain(Runnable runnable);

    public abstract PacketHandler getPacketHandler(Identifier packet);

    @NotNull
    public SchematicHandler getSchematicHandler() {
        return new CommonSchematicHandler();
    }
}
