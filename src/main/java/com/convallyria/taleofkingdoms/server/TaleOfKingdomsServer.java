package com.convallyria.taleofkingdoms.server;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.listener.GameInstanceListener;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingBankerInteractPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingBuildKingdomPacket;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingBuyItemPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingFixGuildPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingHunterPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingInnkeeperPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingSignContractPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingToggleSellGuiPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.outgoing.OutgoingInstanceSyncPacketHandler;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.fabricmc.api.DedicatedServerModInitializer;

public class TaleOfKingdomsServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        this.registerPacketHandlers();
        this.registerListeners();
    }

    private void registerPacketHandlers() {
        registerHandler(new IncomingBankerInteractPacketHandler());
        registerHandler(new IncomingBuildKingdomPacket());
        registerHandler(new IncomingBuyItemPacketHandler());
        registerHandler(new IncomingFixGuildPacketHandler());
        registerHandler(new IncomingHunterPacketHandler());
        registerHandler(new IncomingInnkeeperPacketHandler());
        registerHandler(new IncomingSignContractPacketHandler());
        registerHandler(new IncomingToggleSellGuiPacketHandler());

        registerHandler(new OutgoingInstanceSyncPacketHandler());
    }

    private void registerListeners() {
        new GameInstanceListener();
    }

    protected void registerHandler(ServerPacketHandler serverPacketHandler) {
        TaleOfKingdoms.getAPI().registerServerHandler(serverPacketHandler);
    }

    private void registerTasks() {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        api.getScheduler().repeating(server -> {
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                server.getPlayerManager().getPlayerList().forEach(player -> {
                    ServerConquestInstance.sync(player, instance);
                    TaleOfKingdoms.LOGGER.info("Synced player data");
                });
            });
        }, 20, 1000);
    }
}
