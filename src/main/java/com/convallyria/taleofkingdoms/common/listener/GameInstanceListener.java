package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.event.GameInstanceCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerJoinCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerLeaveCallback;
import com.convallyria.taleofkingdoms.common.event.tok.KingdomStartCallback;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.Connection;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.CompletableFuture;

@OnlyIn(Dist.DEDICATED_SERVER)
public class GameInstanceListener extends Listener {

    public GameInstanceListener() {
        GameInstanceCallback.EVENT.register(gameInstance -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> api.setServer(gameInstance));
        });

        PlayerJoinCallback.EVENT.register((connection, player) -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                if (!api.executeOnDedicatedServer(() -> {
                    DedicatedServer server = api.getServer().get();
                    boolean loaded = load(server.getLevelIdName(), api);
                    File conquestFile = new File(api.getDataFolder() + "worlds/" + server.getLevelIdName() + ".conquestworld");
                    if (loaded) {
                        // Already exists
                        Gson gson = TaleOfKingdoms.getAPI().get().getMod().getGson();
                        try {
                            // Load from json into class
                            ConquestInstance instance = null;
                            try (BufferedReader reader = new BufferedReader(new FileReader(conquestFile))) {
                                instance = gson.fromJson(reader, ServerConquestInstance.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // Check if file exists, but values don't. Game probably crashed?
                            if ((instance == null || instance.getName() == null) || !instance.isLoaded()) {
                                this.create(connection, api, player, server, conquestFile).thenAccept(done -> {
                                    api.getConquestInstanceStorage().mostRecentInstance().ifPresent(conquestInstance -> {
                                        ServerConquestInstance serverConquestInstance = (ServerConquestInstance) conquestInstance;
                                        serverConquestInstance.reset(player);
                                        serverConquestInstance.sync(player, connection);
                                    });
                                });
                            } else {
                                if (api.getConquestInstanceStorage().getConquestInstance(server.getLevelIdName()).isEmpty()) {
                                    api.getConquestInstanceStorage().addConquest(server.getLevelIdName(), instance, true);
                                }

                                api.getConquestInstanceStorage().getConquestInstance(server.getLevelIdName()).ifPresent(conquestInstance -> {
                                    ServerConquestInstance serverConquestInstance = (ServerConquestInstance) conquestInstance;
                                    if (!serverConquestInstance.hasPlayer(player.getUUID())) {
                                        serverConquestInstance.reset(player);
                                    }
                                    serverConquestInstance.sync(player, connection);
                                });
                            }
                        } catch (JsonSyntaxException | JsonIOException e) {
                            e.printStackTrace();
                        }
                    }
                })) {
                    TaleOfKingdoms.LOGGER.error("Dedicated server not present!");
                }
            });
        });

        PlayerLeaveCallback.EVENT.register(player -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                api.executeOnDedicatedServer(() -> {
                    api.getServer().flatMap(server -> api.getConquestInstanceStorage()
                            .getConquestInstance(server.getLevelIdName())).ifPresent(conquestInstance -> {
                        conquestInstance.save(api);
                    });
                });
            });
        });
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

    private CompletableFuture<Void> create(Connection connection, TaleOfKingdomsAPI api, ServerPlayer player, DedicatedServer server, File toSave) {
        // int topY = server.getOverworld().getTopY(Heightmap.Type.MOTION_BLOCKING, 0, 0);
        BlockPos pastePos = player.blockPosition().subtract(new Vec3i(0, 12, 0));
        ServerConquestInstance instance = new ServerConquestInstance(server.getLevelIdName(), server.name(), null, null, player.blockPosition().offset(0, 1, 0));
        try (Writer writer = new FileWriter(toSave)) {
            Gson gson = api.getMod().getGson();
            gson.toJson(instance, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        api.getConquestInstanceStorage().addConquest(server.getLevelIdName(), instance, true);
        return api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, player, pastePos).thenAccept(oi -> {
            BlockPos start = new BlockPos(oi.maxX(), oi.maxY(), oi.maxZ());
            BlockPos end = new BlockPos(oi.minX(), oi.minY(), oi.minZ());
            instance.setStart(start);
            instance.setEnd(end);
            instance.setBankerCoins(player.getUUID(), 0);
            instance.setCoins(player.getUUID(), 0);
            instance.setFarmerLastBread(player.getUUID(), 0);
            instance.setHasContract(player.getUUID(), false);
            instance.setWorthiness(player.getUUID(), 0);
            
            TaleOfKingdoms.LOGGER.info("Summoning citizens of the realm...");
            KingdomStartCallback.EVENT.invoker().kingdomStart(player, instance); // Call kingdom start event
            instance.setLoaded(true);
            instance.reset(player);
            instance.sync(player, connection);
            instance.save(api);
        }).exceptionally(error -> {
            error.printStackTrace();
            return null;
        });
    }
}
