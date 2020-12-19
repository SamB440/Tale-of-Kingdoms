package com.convallyria.taleofkingdoms.server;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.listener.GameInstanceListener;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingSignContractPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.outgoing.OutgoingInstanceSyncPacketHandler;
import net.fabricmc.api.DedicatedServerModInitializer;

public class TaleOfKingdomsServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        this.registerPacketHandlers();
        this.registerListeners();
    }

    private void registerPacketHandlers() {
        registerHandler(new OutgoingInstanceSyncPacketHandler());
        registerHandler(new IncomingSignContractPacketHandler());
    }

    private void registerListeners() {
        new GameInstanceListener();
    }

    protected void registerHandler(ServerPacketHandler serverPacketHandler) {
        TaleOfKingdoms.getAPI().ifPresent(api -> api.registerServerHandler(serverPacketHandler));
    }
}
