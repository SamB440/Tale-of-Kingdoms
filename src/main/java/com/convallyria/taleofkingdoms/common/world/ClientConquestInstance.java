package com.convallyria.taleofkingdoms.common.world;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.google.common.collect.ImmutableList;
import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ClientConquestInstance extends ConquestInstance {

    private int coins;
    private int bankerCoins;
    private long farmerLastBread;
    private boolean hasContract;
    private int worthiness;
    private List<UUID> hunterUUIDs;

    public ClientConquestInstance(String world, String name, BlockPos start, BlockPos end, BlockPos origin) {
        super(world, name, start, end, origin);
        this.hunterUUIDs = new ArrayList<>();
    }

    @Override
    public int getCoins(UUID uuid) {
        return coins;
    }

    @Override
    public int getBankerCoins(UUID uuid) { return bankerCoins; }

    @Override
    public void setBankerCoins(UUID uuid, int bankerCoins) { this.bankerCoins = bankerCoins; }

    @Override
    public void setCoins(UUID uuid, int coins) {
        this.coins = coins;
    }

    @Override
    public void addCoins(UUID uuid, int coins) {
        this.coins = this.coins + coins;
    }

    @Override
    public long getFarmerLastBread(UUID uuid) {
        return farmerLastBread;
    }

    @Override
    public void setFarmerLastBread(UUID uuid, long day) {
        this.farmerLastBread = day;
    }

    @Override
    public boolean hasContract(UUID uuid) {
        return hasContract;
    }

    @Override
    public void setHasContract(UUID uuid, boolean hasContract) {
        this.hasContract = hasContract;
    }

    @Override
    public int getWorthiness(UUID uuid) {
        return worthiness;
    }

    @Override
    public void setWorthiness(UUID uuid, int worthiness) {
        this.worthiness = worthiness;
    }

    @Override
    public void addWorthiness(UUID uuid, int worthiness) {
        this.worthiness = this.worthiness + worthiness;
    }

    public void addHunter(Entity entity) {
        this.hunterUUIDs.add(entity.getUuid());
    }

    public ImmutableList<UUID> getHunterUUIDs() {
        if (hunterUUIDs == null) this.hunterUUIDs = new ArrayList<>();
        return ImmutableList.copyOf(hunterUUIDs);
    }

    public void removeHunter(Entity entity) {
        this.hunterUUIDs.remove(entity.getUuid());
    }

    public CompletableFuture<Void> rebuild(ServerPlayerEntity serverPlayerEntity, TaleOfKingdomsAPI api, boolean entities) {
        BlockPos origin = getOrigin();
        BlockVector3 blockVector3 = BlockVector3.at(origin.getX(), origin.getY(), origin.getZ());
        return api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, serverPlayerEntity, blockVector3).thenAccept(oi -> {
            api.executeOnServer(() -> {
                BlockVector3 max = oi.getRegion().getMaximumPoint();
                BlockVector3 min = oi.getRegion().getMinimumPoint();

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
                            World world = serverPlayerEntity.getServer().getOverworld();
                            Chunk chunk = world.getChunk(blockPos);
                            BlockEntity tileEntity = chunk.getBlockEntity(blockPos);
                            if (tileEntity instanceof SignBlockEntity) {
                                SignBlockEntity signTileEntity = (SignBlockEntity) tileEntity;
                                Tag line1 = signTileEntity.toInitialChunkDataTag().get("Text1");
                                if (line1 == null) continue;
                                // Doesn't seem to be a way to get the plain string...
                                if (line1.toText().getString().equals("'{\"text\":\"[Spawn]\"}'")) {
                                    Tag line2 = signTileEntity.toInitialChunkDataTag().get("Text2");
                                    if (entities) {
                                        String entityName = line2.toText().getString().replace("'{\"text\":\"", "").replace("\"}'", "");
                                        BlockPos pos = new BlockPos(x + 0.5, y, z + 0.5);
                                        try {
                                            EntityType type = (EntityType<?>) EntityTypes.class.getField(entityName.toUpperCase()).get(EntityTypes.class);
                                            if (type != EntityTypes.GUILDGUARD && type != EntityTypes.GUILDARCHER) {
                                                Optional<? extends Entity> guildEntity = getGuildEntity(world, type);
                                                if (type == EntityTypes.GUILDMASTER) {
                                                    guildEntity = getGuildMaster(world);
                                                }

                                                if (!guildEntity.isPresent()) {
                                                    EntityUtils.spawnEntity(type, serverPlayerEntity, pos);
                                                }
                                            } else EntityUtils.spawnEntity(type, serverPlayerEntity, pos);
                                        } catch (IllegalAccessException | NoSuchFieldException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    world.breakBlock(blockPos, false);
                                } else if (line1.toText().getString().equals("'{\"text\":\"[Event]\"}'")) {
                                    Tag line2 = signTileEntity.toInitialChunkDataTag().get("Text2");
                                    String event = line2.toText().getString().replace("'{\"text\":\"", "").replace("\"}'", "");
                                    if (event.equals("ReficuleGateway")) {
                                        world.breakBlock(blockPos, false);
                                    }
                                }
                            }
                        }
                    }
                }
            });
        });
    }
}
