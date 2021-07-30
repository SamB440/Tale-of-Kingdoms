package com.convallyria.taleofkingdoms.common.world;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.LoneVillagerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.generator.processor.GatewayStructureProcessor;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.schematic.SchematicOptions;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.quest.Quest;
import com.google.gson.Gson;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.JigsawReplacementStructureProcessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public abstract class ConquestInstance {

    private final String world;
    private final String name;
    private boolean hasLoaded;
    private BlockPos start;
    private BlockPos end;
    private final BlockPos origin;
    private List<UUID> loneVillagersWithRooms;
    private boolean underAttack;
    private final List<BlockPos> reficuleAttackLocations;
    private final List<UUID> reficuleAttackers;
    private boolean hasRebuilt;
    private transient Map<UUID, Quest> activeQuests;

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
        this.loneVillagersWithRooms = new ArrayList<>();
        this.reficuleAttackLocations = new ArrayList<>();
        this.reficuleAttackers = new ArrayList<>();
        this.activeQuests = new HashMap<>();
    }

    public boolean hasActiveQuest(UUID uuid) {
        if (activeQuests == null) this.activeQuests = new HashMap<>();
        return activeQuests.containsKey(uuid);
    }

    public void addActiveQuest(UUID uuid, Quest quest) {
        if (activeQuests == null) this.activeQuests = new HashMap<>();
        activeQuests.put(uuid, quest);
    }

    public boolean isClient() {
        return this instanceof ClientConquestInstance;
    }
    
    public boolean isServer() {
        return this instanceof ServerConquestInstance;
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

    public void setStart(BlockPos start) {
        this.start = start;
    }

    public BlockPos getEnd() {
        return end;
    }

    public void setEnd(BlockPos end) {
        this.end = end;
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public boolean canAttack() {
        return canAttack(null);
    }

    public boolean canAttack(UUID uuid) {
        return getWorthiness(uuid) >= (1500.0F / 2) && !isUnderAttack() && !hasRebuilt;
    }

    /**
     * @see #hasAttacked(UUID)
     */
    public boolean hasAttacked() {
        return hasAttacked(null);
    }
    
    /**
     * Returns true if and only if the guild is not currently under attack and the worthiness of the player is greater than 750
     * @param uuid player uuid to check, nullable
     * @return If the guild has been attacked
     */
    public boolean hasAttacked(UUID uuid) {
        return !isUnderAttack() && getWorthiness(uuid) > 750;
    }

    public void attack(PlayerEntity player, ServerWorldAccess world) {
        if (canAttack(player.getUuid())) {
            TaleOfKingdoms.LOGGER.info("Initiating guild attack for player " + player.getName());
            EntityUtils.spawnEntity(EntityTypes.GUILDMASTER_DEFENDER, world, player.getBlockPos());
            this.underAttack = true;
            Translations.GUILDMASTER_HELP.send(player);

            Identifier gateway = new Identifier(TaleOfKingdoms.MODID, "gateway/gateway");
            world.toServerWorld().getStructureManager().getStructure(gateway).ifPresent(structure -> {
                for (BlockPos reficuleAttackLocation : reficuleAttackLocations) {
                    StructurePlacementData structurePlacementData = new StructurePlacementData();
                    structurePlacementData.addProcessor(GatewayStructureProcessor.INSTANCE);
                    structurePlacementData.addProcessor(JigsawReplacementStructureProcessor.INSTANCE);
                    structurePlacementData.addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR);
                    BlockPos newPos = reficuleAttackLocation.subtract(new Vec3i(6, 1, 6));
                    structure.place(world, newPos, newPos, structurePlacementData, ThreadLocalRandom.current(), Block.NOTIFY_ALL);
                }
            });
        }
    }

    /**
     * @return If the guild is currently under attack
     */
    public boolean isUnderAttack() {
        return underAttack;
    }

    public void setUnderAttack(boolean underAttack) {
        this.underAttack = underAttack;
    }

    public List<UUID> getLoneVillagersWithRooms() {
        if (loneVillagersWithRooms == null) this.loneVillagersWithRooms = new ArrayList<>();
        return loneVillagersWithRooms;
    }

    public void addLoneVillagerWithRoom(LoneVillagerEntity entity) {
        if (loneVillagersWithRooms == null) this.loneVillagersWithRooms = new ArrayList<>();
        loneVillagersWithRooms.add(entity.getUuid());
    }

    public List<BlockPos> getReficuleAttackLocations() {
        return reficuleAttackLocations;
    }

    public List<UUID> getReficuleAttackers() {
        return reficuleAttackers;
    }

    /**
     * @return If the guild has been rebuilt
     */
    public boolean hasRebuilt() {
        return hasRebuilt;
    }

    public void setRebuilt(boolean hasRebuilt) {
        this.hasRebuilt = hasRebuilt;
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

    public Optional<GuildMasterEntity> getGuildMaster(World world) {
        if (start == null || end == null) return Optional.empty();
        Box box = new Box(getStart(), getEnd());
        return world.getEntitiesByType(EntityTypes.GUILDMASTER, box, guildMaster -> !guildMaster.isFireImmune()).stream().findFirst();
    }

    public Optional<? extends Entity> getGuildEntity(World world, EntityType<?> type) {
        if (start == null || end == null) return Optional.empty();
        Box box = new Box(getStart(), getEnd());
        return world.getEntitiesByType(type, box, entity -> true).stream().findFirst();
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
        if (start == null || end == null) return false; // Probably still pasting.
        BlockBox blockBox = new BlockBox(end.getX(), end.getY(), end.getZ(), start.getX(), start.getY(), start.getZ());
        return blockBox.contains(pos);
    }

    public CompletableFuture<BlockBox> rebuild(ServerPlayerEntity serverPlayerEntity, TaleOfKingdomsAPI api, SchematicOptions... options) {
        return api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, serverPlayerEntity, getOrigin().subtract(new Vec3i(0, 13, 0)), options);
    }

    public void save(TaleOfKingdomsAPI api) {
        File file = new File(api.getDataFolder() + "worlds" + File.separator + world + ".conquestworld");
        try (Writer writer = new FileWriter(file)) {
            Gson gson = api.getMod().getGson();
            gson.toJson(this, writer);
            TaleOfKingdoms.LOGGER.info("Saved data");
        } catch (IOException e) {
            TaleOfKingdoms.LOGGER.error("Error saving data: ", e);
            e.printStackTrace();
        }
    }
}