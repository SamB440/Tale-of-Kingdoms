package com.convallyria.taleofkingdoms.server;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.listener.GameInstanceListener;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.outgoing.OutgoingInstanceSyncPacketHandler;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaleOfKingdomsServer implements DedicatedServerModInitializer {

    private Map<Identifier, ServerPacketHandler> packetHandlers = new ConcurrentHashMap<>();

    @Override
    public void onInitializeServer() {
        this.registerPacketHandlers();
        this.registerListeners();
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            api.setServerMod(this);
        });
    }

    public ServerPacketHandler getHandler(Identifier identifier) {
        return packetHandlers.get(identifier);
    }

    private void registerPacketHandlers() {
        registerHandler(new OutgoingInstanceSyncPacketHandler());
    }

    private void registerListeners() {
        new GameInstanceListener();
    }

    protected void registerHandler(ServerPacketHandler serverPacketHandler) {
        packetHandlers.put(serverPacketHandler.getPacket(), serverPacketHandler);
    }
}
