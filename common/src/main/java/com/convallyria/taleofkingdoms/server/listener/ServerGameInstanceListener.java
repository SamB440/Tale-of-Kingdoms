package com.convallyria.taleofkingdoms.server.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.event.GameInstanceCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerJoinCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerJoinWorldCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerLeaveCallback;
import com.convallyria.taleofkingdoms.common.event.tok.KingdomStartCallback;
import com.convallyria.taleofkingdoms.common.gamerule.TOKGameRules;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServer;
import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServerAPI;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.SERVER)
public class ServerGameInstanceListener extends Listener {

    public ServerGameInstanceListener() {
        final TaleOfKingdomsServerAPI api = TaleOfKingdomsServer.getAPI();
        GameInstanceCallback.EVENT.register(api::setServer);

        PlayerJoinCallback.EVENT.register((no, player) -> api.executeOnDedicatedServer(() -> {
            DedicatedServer server = api.getServer();
            boolean loaded = load(server.getLevelIdName(), api);
            File conquestFile = new File(api.getDataFolder() + "worlds/" + server.getLevelIdName() + ConquestInstance.FILE_TYPE);
            if (loaded) {
                // Already exists
                Gson gson = api.getMod().getGson();
                try {
                    // Load from json into class
                    ConquestInstance instance = null;
                    try (BufferedReader reader = new BufferedReader(new FileReader(conquestFile))) {
                        instance = gson.fromJson(reader, ConquestInstance.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Check if file exists, but values don't. Game probably crashed?
                    if (instance == null || !instance.isLoaded()) {
                        this.create(api, player, server);
                    } else {
                        if (api.getConquestInstanceStorage().getConquestInstance(server.getLevelIdName()).isEmpty()) {
                            api.getConquestInstanceStorage().addConquest(server.getLevelIdName(), instance, true);
                        }
                    }
                } catch (JsonSyntaxException | JsonIOException e) {
                    e.printStackTrace();
                }
            }
        }));

        PlayerJoinWorldCallback.EVENT.register(player -> {
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) {
                    if (!instance.hasPlayer(player.getUUID())) {
                        instance.reset(player);
                    }

                    ServerConquestInstance.sync(player, instance);
                }
            });
        });

        PlayerLeaveCallback.EVENT.register(player -> api.executeOnDedicatedServer(() -> {
            final DedicatedServer server = api.getServer();
            api.getConquestInstanceStorage().getConquestInstance(server.getLevelIdName()).ifPresent(conquestInstance -> {
                conquestInstance.save(server.getLevelIdName());
            });
        }));
    }

    private boolean load(String worldName, TaleOfKingdomsAPI api) {
        File file = new File(api.getDataFolder() + "worlds/" + worldName + ConquestInstance.FILE_TYPE);
        // Check if this world has been loaded or not
        if (!file.exists()) {
            try {
                // If not, create new file
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            // It already exists.
            return true;
        }
    }

    private CompletableFuture<Void> create(TaleOfKingdomsAPI api, ServerPlayer player, DedicatedServer server) {
        if (server.getGameRules().getBoolean(TOKGameRules.AUTO_GENERATE_GUILD)) {
            // The guild generates naturally as a structure; the conquest is named when a player
            // signs the contract with the Guild Master.
            TaleOfKingdoms.LOGGER.info("Guild will generate automatically; starting conquest for world: " + server.getLevelIdName());
            ConquestInstance instance = new ConquestInstance(null, null, null, null);
            instance.reset(player);
            instance.setLoaded(true);
            api.getConquestInstanceStorage().addConquest(server.getLevelIdName(), instance, true);
            instance.save(server.getLevelIdName());
            return CompletableFuture.completedFuture(null);
        }

        // int topY = server.getOverworld().getTopY(Heightmap.Type.MOTION_BLOCKING, 0, 0);
        BlockPos pastePos = player.blockPosition().subtract(new Vec3i(0, 20, 0));
        ConquestInstance instance = new ConquestInstance(server.name(), null, null, player.blockPosition().offset(0, 1, 0));
        instance.reset(player);
        api.getConquestInstanceStorage().addConquest(server.getLevelIdName(), instance, true);
        return api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, player, pastePos).thenAccept(oi -> {
            BlockPos start = new BlockPos(oi.maxX(), oi.maxY(), oi.maxZ());
            BlockPos end = new BlockPos(oi.minX(), oi.minY(), oi.minZ());
            instance.setStart(start);
            instance.setEnd(end);
            
            TaleOfKingdoms.LOGGER.info("Summoning citizens of the realm...");
            KingdomStartCallback.EVENT.invoker().kingdomStart(player, instance); // Call kingdom start event
            instance.setLoaded(true);
            /*instance.reset(player);
            instance.sync(player);*/
            instance.save(server.getLevelIdName());
        }).exceptionally(error -> {
            error.printStackTrace();
            return null;
        });
    }
}
