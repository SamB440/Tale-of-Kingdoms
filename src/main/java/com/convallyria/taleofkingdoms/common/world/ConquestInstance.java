package com.convallyria.taleofkingdoms.common.world;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.google.gson.Gson;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.Tag;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class ConquestInstance {

    private final String world;
    private final String name;
    private boolean hasLoaded;
    private final BlockPos start;
    private final BlockPos end;
    private final BlockPos origin;

    public ConquestInstance(String world, String name, BlockPos start, BlockPos end, BlockPos origin) {
        Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI()
                .map(TaleOfKingdomsAPI::getConquestInstanceStorage)
                .orElseThrow(() -> new IllegalArgumentException("API not present"))
                .getConquestInstance(world);
        if (instance.isPresent() && instance.get().isLoaded()) throw new IllegalArgumentException("World already registered");
        this.world = world;
        this.name = name;
        this.start = start;
        this.end = end;
        this.origin = origin;
    }

    public String getWorld() {
        return world;
    }

    public String getName() {
        return name;
    }

    public boolean isLoaded() {
        return hasLoaded;
    }

    public void setLoaded(boolean loaded) {
        this.hasLoaded = loaded;
    }

    public BlockPos getStart() {
        return start;
    }

    public BlockPos getEnd() {
        return end;
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public abstract int getCoins(UUID uuid);

    public abstract int getBankerCoins(UUID uuid);

    public abstract void setBankerCoins(UUID uuid, int bankerCoins);

    public abstract void setCoins(UUID uuid, int coins);

    public abstract void addCoins(UUID uuid, int coins);

    public abstract long getFarmerLastBread(UUID uuid);

    public abstract void setFarmerLastBread(UUID uuid, long day);

    public abstract boolean hasContract(UUID uuid);

    public abstract void setHasContract(UUID uuid, boolean hasContract);

    public abstract int getWorthiness(UUID uuid);

    public abstract void setWorthiness(UUID uuid, int worthiness);

    public abstract void addWorthiness(UUID uuid, int worthiness);

    public int getCoins() {
        return getCoins(null);
    }

    public int getBankerCoins() {
        return getBankerCoins(null);
    }

    public void setBankerCoins(int bankerCoins) {
        setBankerCoins(null, bankerCoins);
    }

    public void setCoins(int coins) {
        setCoins(null, coins);
    }

    public void addCoins(int coins) {
        addCoins(null, coins);
    }

    public long getFarmerLastBread() {
        return getFarmerLastBread(null);
    }

    public void setFarmerLastBread(long day) {
        setFarmerLastBread(null, day);
    }

    public boolean hasContract() {
        return hasContract(null);
    }

    public void setHasContract(boolean hasContract) {
        setHasContract(null, hasContract);
    }

    public int getWorthiness() {
        return getWorthiness(null);
    }

    public void setWorthiness(int worthiness) {
        setWorthiness(null, worthiness);
    }

    public void addWorthiness(int worthiness) {
        addWorthiness(null, worthiness);
    }

    private List<BlockPos> validRest;

    /**
     * Gets valid sleep area locations. This gets the sign, not the bed head.
     * @param player the player
     * @return list of signs where sleeping is allowed
     */
    @NotNull
    public List<BlockPos> getSleepLocations(PlayerEntity player) {
        if (validRest == null) validRest = new ArrayList<>();
        if (validRest.isEmpty()) { // Find a valid resting place. This will only run if validRest is empty, which is also saved to file.
            int topBlockX = (Math.max(start.getX(), end.getX()));
            int bottomBlockX = (Math.min(start.getX(), end.getX()));

            int topBlockY = (Math.max(start.getY(), end.getY()));
            int bottomBlockY = (Math.min(start.getY(), end.getY()));

            int topBlockZ = (Math.max(start.getZ(), end.getZ()));
            int bottomBlockZ = (Math.min(start.getZ(), end.getZ()));

            for (int x = bottomBlockX; x <= topBlockX; x++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    for (int y = bottomBlockY; y <= topBlockY; y++) {
                        BlockPos blockPos = new BlockPos(x, y, z);
                        BlockEntity tileEntity = player.getEntityWorld().getChunk(blockPos).getBlockEntity(blockPos);
                        if (tileEntity instanceof BedBlockEntity) {
                            validRest.add(blockPos);
                        }
                    }
                }
            }
        }

        return validRest;
    }

    public List<BlockPos> getValidRest() {
        return validRest;
    }

    /**
     * Checks if an entity is in the guild.
     * @param entity the entity
     * @return true if player is in guild, false if not
     */
    public boolean isInGuild(Entity entity) {
        return isInGuild(entity.getBlockPos());
    }

    /**
     * Checks if a location is in the guild.
     * @param pos the {@link BlockPos}
     * @return true if position is in guild, false if not
     */
    public boolean isInGuild(BlockPos pos) {
        BlockVector3 firstPos = BlockVector3.at(start.getX(), start.getY(), start.getZ());
        BlockVector3 secondPos = BlockVector3.at(end.getX(), end.getY(), end.getZ());
        Region region = new CuboidRegion(firstPos, secondPos);
        BlockVector3 blockVector3 = BlockVector3.at(pos.getX(), pos.getY(), pos.getZ());
        return region.contains(blockVector3);
    }

    public void save(TaleOfKingdomsAPI api) {
        File file = new File(api.getDataFolder() + "worlds/" + world + ".conquestworld");
        try (Writer writer = new FileWriter(file)) {
            Gson gson = TaleOfKingdoms.getAPI().get().getMod().getGson();
            gson.toJson(this, writer);
            TaleOfKingdoms.LOGGER.info("Saved data");
        } catch (IOException e) {
            TaleOfKingdoms.LOGGER.error("Error saving data: ", e);
            e.printStackTrace();
        }
    }
}
