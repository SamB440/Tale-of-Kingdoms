package com.convallyria.taleofkingdoms.server;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.server.listener.ServerGameInstanceListener;
import com.convallyria.taleofkingdoms.server.packet.both.BothSignContractPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingBankerInteractPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingBuildKingdomPacket;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingBuyItemPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingCityBuilderActionPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingFixGuildPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingForemanBuyWorkerPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingForemanCollectPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingHunterPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingInnkeeperPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.incoming.IncomingToggleSellGuiPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.outgoing.OutgoingInstanceSyncPacketHandler;
import com.convallyria.taleofkingdoms.server.packet.outgoing.OutgoingOpenScreenPacketHandler;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.minecraft.server.network.ServerPlayerEntity;

public class TaleOfKingdomsServer implements DedicatedServerModInitializer {

    private static TaleOfKingdomsServerAPI api;

    public static TaleOfKingdomsServerAPI getAPI() {
        return api;
    }

    @Override
    public void onInitializeServer() {
        TaleOfKingdoms.setAPI(api = new TaleOfKingdomsServerAPI(TaleOfKingdoms.getInstance()));
        this.registerPacketHandlers();
        this.registerListeners();
        this.registerTasks();
    }

    private void registerPacketHandlers() {
        registerHandler(new BothSignContractPacketHandler());

        registerHandler(new IncomingBankerInteractPacketHandler());
        registerHandler(new IncomingBuildKingdomPacket());
        registerHandler(new IncomingBuyItemPacketHandler());
        registerHandler(new IncomingCityBuilderActionPacketHandler());
        registerHandler(new IncomingFixGuildPacketHandler());
        registerHandler(new IncomingForemanBuyWorkerPacketHandler());
        registerHandler(new IncomingForemanCollectPacketHandler());
        registerHandler(new IncomingHunterPacketHandler());
        registerHandler(new IncomingInnkeeperPacketHandler());
        registerHandler(new IncomingToggleSellGuiPacketHandler());

        registerHandler(new OutgoingInstanceSyncPacketHandler());
        registerHandler(new OutgoingOpenScreenPacketHandler());
    }

    private void registerListeners() {
        new ServerGameInstanceListener();
    }

    protected void registerHandler(PacketHandler<?> serverPacketHandler) {
        api.registerPacketHandler(EnvType.SERVER, serverPacketHandler);
    }

    private void registerTasks() {
//        api.getScheduler().repeating(server -> {
//            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
//                server.getPlayerManager().getPlayerList().forEach(player -> {
//                    ServerConquestInstance.sync(player, instance);
//                    TaleOfKingdoms.LOGGER.info("Synced player data");
//                });
//            });
//        }, 20, 1000);

        api.getScheduler().repeating(server -> {
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                instance.getGuildPlayers().forEach((id, guildPlayer) -> {
                    final PlayerKingdom kingdom = guildPlayer.getKingdom();
                    if (kingdom == null) return;
                    kingdom.tryTaxCollection(guildPlayer);
                    final ServerPlayerEntity player = server.getPlayerManager().getPlayer(id);
                    if (player != null) {
                        ServerConquestInstance.sync(player, instance);
                    }
                });
            });
        }, 20, 1000);
    }
}
