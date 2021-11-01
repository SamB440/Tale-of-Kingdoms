package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.event.GameInstanceCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerJoinCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerJoinWorldCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerLeaveCallback;
import com.convallyria.taleofkingdoms.common.event.tok.KingdomStartCallback;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.SERVER)
public class GameInstanceListener extends Listener {

    public GameInstanceListener() {
        GameInstanceCallback.EVENT.register(gameInstance -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> api.setServer(gameInstance));
        });

        PlayerJoinCallback.EVENT.register((no, player) -> TaleOfKingdoms.getAPI().ifPresent(api -> {
            if (!api.executeOnDedicatedServer(() -> {
                MinecraftDedicatedServer server = api.getServer().get();
                boolean loaded = load(server.getLevelName(), api);
                File conquestFile = new File(api.getDataFolder() + "worlds/" + server.getLevelName() + ".conquestworld");
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
                            this.create(api, player, server, conquestFile);
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
        }));

        PlayerJoinWorldCallback.EVENT.register(player -> TaleOfKingdoms.getAPI().ifPresent(api -> {
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                if (instance instanceof ServerConquestInstance serverConquestInstance) {
                    if (!serverConquestInstance.hasPlayer(player.getUuid())) {
                        serverConquestInstance.reset(player);
                    }
                    serverConquestInstance.sync(player);
                }
            });
        }));

        PlayerLeaveCallback.EVENT.register(player -> TaleOfKingdoms.getAPI().ifPresent(api -> {
            api.executeOnDedicatedServer(() -> {
                api.getServer().flatMap(server -> api.getConquestInstanceStorage()
                        .getConquestInstance(server.getLevelName())).ifPresent(conquestInstance -> {
                    conquestInstance.save(api);
                    api.getConquestInstanceStorage().quit();
                });
            });
        }));
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

    private CompletableFuture<Void> create(TaleOfKingdomsAPI api, ServerPlayerEntity player, MinecraftDedicatedServer server, File toSave) {
        // int topY = server.getOverworld().getTopY(Heightmap.Type.MOTION_BLOCKING, 0, 0);
        BlockPos pastePos = player.getBlockPos().subtract(new Vec3i(0, 12, 0));
        ServerConquestInstance instance = new ServerConquestInstance(server.getLevelName(), server.getName(), null, null, player.getBlockPos().add(0, 1, 0));
        try (Writer writer = new FileWriter(toSave)) {
            Gson gson = api.getMod().getGson();
            gson.toJson(instance, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        api.getConquestInstanceStorage().addConquest(server.getLevelName(), instance, true);
        return api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, player, pastePos).thenAccept(oi -> {
            BlockPos start = new BlockPos(oi.getMaxX(), oi.getMaxY(), oi.getMaxZ());
            BlockPos end = new BlockPos(oi.getMinX(), oi.getMinY(), oi.getMinZ());
            instance.setStart(start);
            instance.setEnd(end);
            instance.setBankerCoins(player.getUuid(), 0);
            instance.setCoins(player.getUuid(), 0);
            instance.setFarmerLastBread(player.getUuid(), 0);
            instance.setHasContract(player.getUuid(), false);
            instance.setWorthiness(player.getUuid(), 0);
            
            TaleOfKingdoms.LOGGER.info("Summoning citizens of the realm...");
            KingdomStartCallback.EVENT.invoker().kingdomStart(player, instance); // Call kingdom start event
            instance.setLoaded(true);
            /*instance.reset(player);
            instance.sync(player);*/
            instance.save(api);
        }).exceptionally(error -> {
            error.printStackTrace();
            return null;
        });
    }
}
