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
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class TaleOfKingdomsAPI {

    private final TaleOfKingdoms mod;
    private final ConquestInstanceStorage cis;
    private final Map<Class<? extends IManager>, IManager> managers = new HashMap<>();
    private final Map<EnvType, Map<CustomPayload.Id<?>, PacketHandler<?>>> packetHandlers = new HashMap<>();

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

    public void registerPacketHandler(EnvType envType, PacketHandler<?> packet) {
        final Map<CustomPayload.Id<?>, PacketHandler<?>> handlers = packetHandlers.getOrDefault(envType, new HashMap<>());
        handlers.put(packet.getPacket(), packet);
        packetHandlers.put(envType, handlers);
    }

    // Packet explanation
    // getServerPacket gets a packet sent/received by dedicated or integrated server
    // getClientPacket gets a packet sent/received by the client

    public <T extends CustomPayload> PacketHandler<T> getServerPacket(CustomPayload.Id<T> packet) {
        return getPacketHandler(EnvType.SERVER, packet);
    }

    public <T extends CustomPayload> PacketHandler<T> getClientPacket(CustomPayload.Id<T> packet) {
        return getPacketHandler(EnvType.CLIENT, packet);
    }

    private <T extends CustomPayload> PacketHandler<T> getPacketHandler(EnvType envType, CustomPayload.Id<T> packet) {
        final PacketHandler<?> packetHandler = packetHandlers.get(envType).get(packet);
        if (packetHandler == null) {
            throw new IllegalArgumentException("Failed to find packet '" + packet.id().toString() + "' on environment " + envType + "!");
        }
        return (PacketHandler<T>) packetHandler;
    }

    @NotNull
    public SchematicHandler getSchematicHandler() {
        return new CommonSchematicHandler();
    }
}
