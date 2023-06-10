package com.convallyria.taleofkingdoms.server.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.event.GameInstanceCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerJoinCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerJoinWorldCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerLeaveCallback;
import com.convallyria.taleofkingdoms.common.event.tok.KingdomStartCallback;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.SERVER)
public class ServerGameInstanceListener extends Listener {

    public ServerGameInstanceListener() {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        GameInstanceCallback.EVENT.register(api::setServer);

        PlayerJoinCallback.EVENT.register((no, player) -> {
            if (!api.executeOnDedicatedServer(() -> {
                MinecraftDedicatedServer server = api.getServer().get();
                boolean loaded = load(server.getLevelName(), api);
                File conquestFile = new File(api.getDataFolder() + "worlds/" + server.getLevelName() + ".conquestworld");
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
                        if ((instance == null || instance.getName() == null) || !instance.isLoaded()) {
                            this.create(api, player, server);
                        } else {
                            if (api.getConquestInstanceStorage().getConquestInstance(server.getLevelName()).isEmpty()) {
                                api.getConquestInstanceStorage().addConquest(server.getLevelName(), instance, true);
                            }
                        }
                    } catch (JsonSyntaxException | JsonIOException e) {
                        e.printStackTrace();
                    }
                }
            })) {
                TaleOfKingdoms.LOGGER.error("Dedicated server not present!");
            }
        });

        PlayerJoinWorldCallback.EVENT.register(player -> {
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) {
                    if (!instance.hasPlayer(player.getUuid())) {
                        instance.reset(player);
                    }

                    ServerConquestInstance.sync(player, instance);
                }
            });
        });

        PlayerLeaveCallback.EVENT.register(player -> api.executeOnDedicatedServer(() -> api.getServer().ifPresent(server -> {
            api.getConquestInstanceStorage().getConquestInstance(server.getLevelName()).ifPresent(conquestInstance -> {
                conquestInstance.save(server.getLevelName());
            });
        })));
    }

    private boolean load(String worldName, TaleOfKingdomsAPI api) {
        File file = new File(api.getDataFolder() + "worlds/" + worldName + ".conquestworld");
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

    private CompletableFuture<Void> create(TaleOfKingdomsAPI api, ServerPlayerEntity player, MinecraftDedicatedServer server) {
        // int topY = server.getOverworld().getTopY(Heightmap.Type.MOTION_BLOCKING, 0, 0);
        BlockPos pastePos = player.getBlockPos().subtract(new Vec3i(0, 20, 0));
        ConquestInstance instance = new ConquestInstance(server.getName(), null, null, player.getBlockPos().add(0, 1, 0));
        instance.reset(player);
        api.getConquestInstanceStorage().addConquest(server.getLevelName(), instance, true);
        return api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, player, pastePos).thenAccept(oi -> {
            BlockPos start = new BlockPos(oi.getMaxX(), oi.getMaxY(), oi.getMaxZ());
            BlockPos end = new BlockPos(oi.getMinX(), oi.getMinY(), oi.getMinZ());
            instance.setStart(start);
            instance.setEnd(end);
            
            TaleOfKingdoms.LOGGER.info("Summoning citizens of the realm...");
            KingdomStartCallback.EVENT.invoker().kingdomStart(player, instance); // Call kingdom start event
            instance.setLoaded(true);
            /*instance.reset(player);
            instance.sync(player);*/
            instance.save(server.getLevelName());
        }).exceptionally(error -> {
            error.printStackTrace();
            return null;
        });
    }
}
