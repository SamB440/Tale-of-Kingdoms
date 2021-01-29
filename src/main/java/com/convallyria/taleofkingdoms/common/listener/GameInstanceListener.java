package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
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
import com.sk89q.worldedit.math.BlockVector3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.Tag;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.SERVER)
public class GameInstanceListener extends Listener {

    public GameInstanceListener() {
        GameInstanceCallback.EVENT.register(gameInstance -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> api.setServer(gameInstance));
        });

        PlayerJoinCallback.EVENT.register((connection, player) -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                if (!api.executeOnDedicatedServer(() -> {
                    MinecraftDedicatedServer server = api.getServer().get();
                    boolean loaded = load(server.getLevelName(), api);
                    File conquestFile = new File(api.getDataFolder() + "worlds/" + server.getLevelName() + ".conquestworld");
                    if (loaded) {
                        // Already exists
                        Gson gson = TaleOfKingdoms.getAPI().get().getMod().getGson();
                        try {
                            // Load from json into class
                            BufferedReader reader = new BufferedReader(new FileReader(conquestFile));
                            ConquestInstance instance = gson.fromJson(reader, ServerConquestInstance.class);
                            // Check if file exists, but values don't. Game probably crashed?
                            if ((instance == null || instance.getName() == null) || !instance.isLoaded()) {
                                this.create(connection, api, player, server).thenAccept(done -> {
                                    api.getConquestInstanceStorage().getConquestInstance(server.getLevelName()).ifPresent(conquestInstance -> {
                                        ServerConquestInstance serverConquestInstance = (ServerConquestInstance) conquestInstance;
                                        serverConquestInstance.reset(player);
                                        serverConquestInstance.sync(player, connection);
                                    });
                                });
                            } else {
                                if (!api.getConquestInstanceStorage().getConquestInstance(server.getLevelName()).isPresent()) {
                                    api.getConquestInstanceStorage().addConquest(server.getLevelName(), instance, true);
                                }

                                api.getConquestInstanceStorage().getConquestInstance(server.getLevelName()).ifPresent(conquestInstance -> {
                                    ServerConquestInstance serverConquestInstance = (ServerConquestInstance) conquestInstance;
                                    if (!serverConquestInstance.hasPlayer(player.getUuid())) {
                                        serverConquestInstance.reset(player);
                                    }
                                    serverConquestInstance.sync(player, connection);
                                });
                            }
                        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
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
                            .getConquestInstance(server.getLevelName())).ifPresent(conquestInstance -> {
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

    private CompletableFuture<Void> create(ClientConnection connection, TaleOfKingdomsAPI api, ServerPlayerEntity player, MinecraftDedicatedServer server) {
        int topY = server.getOverworld().getTopY(Heightmap.Type.MOTION_BLOCKING, 0, 0);
        return api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, player, BlockVector3.at(0, topY, 0)).thenAccept(oi -> {
            BlockVector3 max = oi.getRegion().getMaximumPoint();
            BlockVector3 min = oi.getRegion().getMinimumPoint();
            BlockPos start = new BlockPos(max.getBlockX(), max.getBlockY(), max.getBlockZ());
            BlockPos end = new BlockPos(min.getBlockX(), min.getBlockY(), min.getBlockZ());
            ServerConquestInstance instance = new ServerConquestInstance(server.getLevelName(), server.getName(), start, end, new BlockPos(0, topY, 0));
            instance.setBankerCoins(player.getUuid(), 0);
            instance.setCoins(player.getUuid(), 0);
            instance.setFarmerLastBread(player.getUuid(), 0);
            instance.setHasContract(player.getUuid(), false);
            instance.setWorthiness(player.getUuid(), 0);

            try {
                api.getConquestInstanceStorage().addConquest(server.getLevelName(), instance, true);
                TaleOfKingdoms.LOGGER.info("Summoning citizens of the realm...");
                int topBlockX = (Math.max(max.getBlockX(), min.getBlockX()));
                int bottomBlockX = (Math.min(max.getBlockX(), min.getBlockX()));

                int topBlockY = (Math.max(max.getBlockY(), min.getBlockY()));
                int bottomBlockY = (Math.min(max.getBlockY(), min.getBlockY()));

                int topBlockZ = (Math.max(max.getBlockZ(), min.getBlockZ()));
                int bottomBlockZ = (Math.min(max.getBlockZ(), min.getBlockZ()));

                for (int x = bottomBlockX; x <= topBlockX; x++) {
                    for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                        for (int y = bottomBlockY; y <= topBlockY; y++) {
                            BlockPos blockPos = new BlockPos(x, y, z);
                            BlockEntity tileEntity = server.getOverworld().getChunk(blockPos).getBlockEntity(blockPos);
                            if (tileEntity instanceof SignBlockEntity) {
                                SignBlockEntity signTileEntity = (SignBlockEntity) tileEntity;
                                Tag line1 = signTileEntity.toInitialChunkDataTag().get("Text1");
                                if (line1 != null && line1.toText().getString().equals("'{\"text\":\"[Spawn]\"}'")) {
                                    Tag line2 = signTileEntity.toInitialChunkDataTag().get("Text2");
                                    String entityName = line2.toText().getString().replace("'{\"text\":\"", "").replace("\"}'", ""); // Doesn't seem to be a way to get the plain string...
                                    Class<? extends TOKEntity> entity = (Class<? extends TOKEntity>) Class.forName("com.convallyria.taleofkingdoms.common.entity.guild." + entityName + "Entity");
                                    Constructor constructor = entity.getConstructor(EntityType.class, World.class);
                                    EntityType type = (EntityType) EntityTypes.class.getField(entityName.toUpperCase()).get(EntityTypes.class);
                                    TOKEntity toSpawn = (TOKEntity) constructor.newInstance(type, server.getOverworld());
                                    toSpawn.setPos(x + 0.5, y, z + 0.5);
                                    server.getOverworld().spawnEntity(toSpawn);
                                    server.getOverworld().breakBlock(blockPos, false);
                                    toSpawn.refreshPositionAfterTeleport(x + 0.5, y, z + 0.5);
                                    TaleOfKingdoms.LOGGER.info("Spawned entity " + entityName + " " + toSpawn.toString() + " " + toSpawn.getX() + "," + toSpawn.getY() + "," + toSpawn.getZ());
                                }
                            }
                        }
                    }
                }

                KingdomStartCallback.EVENT.invoker().kingdomStart(player, instance); // Call kingdom start event
                instance.setLoaded(true);
                instance.reset(player);
                instance.sync(player, connection);
                instance.save(api);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }).exceptionally(error -> {
            error.printStackTrace();
            return null;
        });
    }
}
