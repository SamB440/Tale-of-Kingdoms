package com.convallyria.taleofkingdoms.server;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.listener.GameInstanceListener;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingBankerInteractPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingBuyItemPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingFixGuildPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingHunterPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingInnkeeperPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingSignContractPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingToggleSellGuiPacketHandler;
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
        registerHandler(new IncomingFixGuildPacketHandler());
        registerHandler(new IncomingToggleSellGuiPacketHandler());
        registerHandler(new IncomingBuyItemPacketHandler());
        registerHandler(new IncomingBankerInteractPacketHandler());
        registerHandler(new IncomingHunterPacketHandler());
        registerHandler(new IncomingInnkeeperPacketHandler());
    }

    private void registerListeners() {
        new GameInstanceListener();
    }

    protected void registerHandler(ServerPacketHandler serverPacketHandler) {
        TaleOfKingdoms.getAPI().ifPresent(api -> api.registerServerHandler(serverPacketHandler));
    }

    private void registerTasks() {
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            api.getScheduler().repeating(server -> {
                api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                    ServerConquestInstance serverConquestInstance = (ServerConquestInstance) instance;
                    server.getPlayerManager().getPlayerList().forEach(player -> {
                        serverConquestInstance.sync(player);
                        TaleOfKingdoms.LOGGER.info("Synced player data");
                    });
                });
            }, 20, 1000);
        });
    }
}
