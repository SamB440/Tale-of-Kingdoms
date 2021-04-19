package com.convallyria.taleofkingdoms.common.schematic.blocky;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.schematic.blocky.block.NBTBlock;
import com.convallyria.taleofkingdoms.common.schematic.blocky.material.BlockDataMaterial;
import com.convallyria.taleofkingdoms.common.schematic.blocky.material.DirectionalBlockDataMaterial;
import com.convallyria.taleofkingdoms.common.schematic.blocky.material.MultipleFacingBlockDataMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A utility class that previews and pastes schematics block-by-block with asynchronous support.
 * <br></br>
 * @version 2.0.3
 * @author SamB440 - Schematic previews, centering and pasting block-by-block, class itself
 * @author brainsynder - 1.13 Palette Schematic Reader
 * @author Math0424 - Rotation calculations
 * @author Jojodmo - Legacy (< 1.12) Schematic Reader
 */
public class SchematicLoader {

    private final TaleOfKingdomsAPI api;
    private final File schematic;

    private short width = 0;
    private short height = 0;
    private short length = 0;

    private byte[] blockDatas;

    private final LinkedHashMap<Vec3d, NBTBlock> nbtBlocks = new LinkedHashMap<>();
    private final LinkedHashMap<Integer, BlockState> blocks = new LinkedHashMap<>();

    /**
     * @param api your api instance
     * @param schematic file to the schematic
     */
    public SchematicLoader(TaleOfKingdomsAPI api, File schematic) {
        this.api = api;
        this.schematic = schematic;
    }

    /**
     * Pastes a schematic, with a specified time
     * @param paster player pasting
     * @param time time in ticks to paste blocks
     * @return collection of locations where schematic blocks will be pasted, null if schematic locations will replace blocks
     * @throws SchematicNotLoadedException when schematic has not yet been loaded
     * @see #loadSchematic()
     */
    @Nullable
    public Collection<BlockPos> pasteSchematic(BlockPos loc,
                                               ServerPlayerEntity paster,
                                               int time,
                                               Options... option) throws SchematicNotLoadedException {
        try {

            if (width == 0
                    || height == 0
                    || length == 0
                    || blocks.isEmpty()) {
                throw new SchematicNotLoadedException("Data has not been loaded yet");
            }

            List<Options> options = Arrays.asList(option);
            Data tracker = new Data();

            LinkedHashMap<Integer, BlockPos> indexLocations = new LinkedHashMap<>();
            LinkedHashMap<Integer, BlockPos> delayedIndexLocations = new LinkedHashMap<>();

            LinkedHashMap<Integer, NBTBlock> nbtData = new LinkedHashMap<>();

            Direction face = getDirection(paster);

            ServerWorld world = paster.getServerWorld();

            /*
             * Loop through all the blocks within schematic size.
             */
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++ y) {
                    for (int z = 0; z < length; ++z) {
                        int index = y * width * length + z * width + x;
                        Vec3d point = new Vec3d(x, y, z);
                        BlockPos location = null;
                        int width2 = width / 2;
                        int length2 = length / 2;
                        switch (face) {
                            case NORTH:
                                location = new BlockPos((x * - 1 + loc.getX()) + width2, y + loc.getY(), (z + loc.getZ()) + length2);
                                break;
                            case EAST:
                                location = new BlockPos((-z + loc.getX()) - length2, y + loc.getY(), (-x - 1) + (width + loc.getZ()) - width2);
                                break;
                            case SOUTH:
                                location = new BlockPos((x + loc.getX()) - width2, y + loc.getY(), (z * - 1 + loc.getZ()) - length2);
                                break;
                            case WEST:
                                location = new BlockPos((z + loc.getX()) + length2, y + loc.getY(), (x + 1) - (width - loc.getZ()) + width2);
                                break;
                            default:
                                break;
                        }

                        BlockState data = blocks.get((int) blockDatas[index]);

                        /*
                         * Ignore blocks that aren't air. Change this if you want the air to destroy blocks too.
                         * Add items to delayedBlocks if you want them placed last, or if they get broken.
                         */
                        if (data != null && data.getBlock() != null && !data.getBlock().is(Blocks.AIR)) {
                            if (NBTMaterial.fromMinecraft(data.getMaterial()) == null || !NBTMaterial.fromMinecraft(data.getMaterial()).isDelayed()) {
                                indexLocations.put(index, location);
                            } else {
                                delayedIndexLocations.put(index, location);
                            }
                        }

                        if (nbtBlocks.containsKey(point)) {
                            nbtData.put(index, nbtBlocks.get(point));
                        }
                    }
                }
            }

            // Make sure delayed blocks are placed last
            indexLocations.putAll(delayedIndexLocations);
            delayedIndexLocations.clear();

            /*
             * Verify location of pasting
             *

            boolean validated = true;

            for (BlockPos validate : indexLocations.values()) {
                Block block = world.getBlockState(validate).getBlock();
                boolean isWater = world.getBlockState(validate.subtract(new Vec3i(0, 1, 0))).getBlock().is(Blocks.WATER);
                BlockPos airPos = new BlockPos(validate.getX(), loc.getY() - 1, validate.getZ());
                boolean isAir = world.getBlockState(airPos).isAir();
                boolean isSolid = !block.is(Blocks.AIR);
                boolean isTransparent = options.contains(Options.IGNORE_TRANSPARENT) && block.getDefaultState().isOpaque() && !block.getDefaultState().isAir();

                if (!options.contains(Options.PLACE_ANYWHERE) && (isWater || isAir || isSolid) && !isTransparent) {
                    // Show fake block where block is interfering with schematic
                    paster.sendBlockChange(validate, Material.RED_STAINED_GLASS.createBlockData());
                    validated = false;
                } else {
                    // Show fake block for air
                    paster.sendBlockChange(validate.getBlock().getLocation(), Material.GREEN_STAINED_GLASS.createBlockData());
                }

                if (!options.contains(Options.PREVIEW)) {
                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                        if (validate.getBlock().getType() == Material.AIR) paster.sendBlockChange(validate.getBlock().getLocation(), Material.AIR.createBlockData());
                    }, 60);
                }
            }

            if (options.contains(Options.PREVIEW)) return indexLocations.values();
            if (!validated) return null;*/

            if (options.contains(Options.REALISTIC)) {
                //TODO
            }

            // Start pasting each block every tick
            tracker.trackCurrentBlock = 0;

            // List of block faces to update *after* the schematic is done pasting.
            List<Block> toUpdate = new ArrayList<>();
            indexLocations.forEach((index, location) -> {
                BlockState data = blocks.get((int) blockDatas[index]);
                if (data.isIn(BlockTags.STAIRS) || data.isIn(BlockTags.FENCES)) {
                    toUpdate.add(data.getBlock());
                }
            });

            api.getScheduler().repeatWhile(server -> {
                // Get the block, set the type, data, and then update the state.
                List<BlockPos> locations = new ArrayList<>(indexLocations.values());
                List<Integer> indexes = new ArrayList<>(indexLocations.keySet());

                BlockPos pos = locations.get(tracker.trackCurrentBlock);
                BlockState data = blocks.get((int) blockDatas[indexes.get(tracker.trackCurrentBlock)]);
                world.setBlockState(pos, data);
                BlockState block = world.getBlockState(locations.get(tracker.trackCurrentBlock));
                if (nbtData.containsKey(indexes.get(tracker.trackCurrentBlock))) {
                    NBTBlock nbtBlock = nbtData.get(indexes.get(tracker.trackCurrentBlock));
                    try {
                        nbtBlock.setData(world, pos, block);
                    } catch (WrongIdException e) {
                        e.printStackTrace();
                    }
                }

                // Update block faces
                BlockDataMaterial blockDataMaterial = null;
                if (block.getBlock() instanceof FacingBlock) {
                    blockDataMaterial = new DirectionalBlockDataMaterial(block);
                } else if (block.getBlock() instanceof HorizontalConnectingBlock) {
                    blockDataMaterial = new MultipleFacingBlockDataMaterial(block);
                }
                if (blockDataMaterial != null) world.setBlockState(pos, blockDataMaterial.update(face));

                // Play block effects. Change to what you want.
                //world.spawnParticles(CloudParticle.CloudFactory)
                //block.getLocation().getWorld().spawnParticle(Particle.CLOUD, block.getLocation(), 6);
                //block.getLocation().getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());

                tracker.trackCurrentBlock++;

                if (tracker.trackCurrentBlock >= locations.size() || tracker.trackCurrentBlock >= indexes.size()) {
                    System.out.println("done");
                    //tracker.trackCurrentBlock = 0;
                    //toUpdate.forEach(b -> b.getState().update(true, true));
                }
            }, tick -> tracker.trackCurrentBlock < indexLocations.size(), 0, time);
            return indexLocations.values();
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }

    /**
     * Pastes a schematic, with the time defaulting to 1 block per second
     * @param location location to paste from
     * @param paster player pasting
     * @param options options to apply to this paste
     * @return list of locations where schematic blocks will be pasted, null if schematic locations will replace blocks
     * @throws SchematicNotLoadedException when schematic has not yet been loaded
     * @see #loadSchematic()
     */
    public Collection<BlockPos> pasteSchematic(BlockPos location, ServerPlayerEntity paster, Options... options) throws SchematicNotLoadedException {
        return pasteSchematic(location, paster, 20, options);
    }

    /**
     * Creates a constant preview of this schematic for the player
     * @param player player
     *
    public void previewSchematic(ServerPlayerEntity player) {
        plugin.getPlayerManagement().setBuilding(player.getUniqueId(), this);
        new BuildTask(plugin, player).start();
    }*/

    /**
     * Loads the schematic file. This should <b>always</b> be used before pasting a schematic.
     * @return schematic (self)
     */
    public SchematicLoader loadSchematic() {

        try {
            // Read the schematic file. Get the width, height, length, blocks, and block data.
            FileInputStream fis = new FileInputStream(schematic);
            CompoundTag nbt = NbtIo.readCompressed(fis);

            width = nbt.getShort("Width");
            height = nbt.getShort("Height");
            length = nbt.getShort("Length");

            blockDatas = nbt.getByteArray("BlockData");

            CompoundTag palette = nbt.getCompound("Palette");
            ListTag tiles = (ListTag) nbt.get("BlockEntities");

            // Load NBT data
            if (tiles != null) {
                for (Tag tile : tiles) {
                    if (tile instanceof CompoundTag) {
                        CompoundTag compound = (CompoundTag) tile;
                        if (!compound.isEmpty()) {
                            NBTMaterial nbtMaterial = NBTMaterial.fromTag(compound);
                            if (nbtMaterial != null) {
                                NBTBlock nbtBlock = nbtMaterial.getNbtBlock(compound);
                                if (!nbtBlock.isEmpty()) nbtBlocks.put(nbtBlock.getOffset(), nbtBlock);
                            }
                        }
                    }
                }
            }

            /*
             * 	Explanation:
             *    The "Palette" is setup like this
             *      "block_data": id (the ID is a Unique ID that WorldEdit gives that
             *                    corresponds to an index in the BlockDatas Array)
             *    So I loop through all the Keys in the "Palette" Compound
             *    and store the custom ID and BlockData in the palette Map
             */
            System.out.println(palette.getReader());
            palette.getKeys().forEach(rawState -> {
                String id = palette.getString(rawState);
                System.out.println(rawState + ";" + id);
                Block blockData = Registry.BLOCK.get(new Identifier(id));
                blocks.put(palette.getInt(rawState), blockData.getDefaultState());
            });

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * @param player player to get direction they are facing
     * @return blockface of cardinal direction player is facing
     */
    private Direction getDirection(ServerPlayerEntity player) {
        float yaw = player.getHeadYaw();
        if (yaw < 0) {
            yaw += 360;
        }

        if (yaw >= 315 || yaw < 45) {
            return Direction.SOUTH;
        } else if (yaw < 135) {
            return Direction.WEST;
        } else if (yaw < 225) {
            return Direction.NORTH;
        } else if (yaw < 315) {
            return Direction.EAST;
        }
        return Direction.NORTH;
    }

    /**
     * Hacky method to avoid "final".
     */
    protected static class Data {
        int trackCurrentBlock;
    }

    /**
     * An enum of options to apply whilst previewing/pasting a schematic.
     */
    public enum Options {
        /**
         * Previews schematic
         */
        PREVIEW,
        /**
         * A realistic building method. Builds from the ground up, instead of in the default slices.
         * <hr></hr>
         * <b>*WIP, CURRENTLY DOES NOTHING*</b>
         * @deprecated does nothing
         */
        @Deprecated
        REALISTIC,
        /**
         * Bypasses the verification check and allows placing anywhere.
         */
        PLACE_ANYWHERE,
        /**
         * Ignores transparent blocks in the placement check
         */
        IGNORE_TRANSPARENT
    }
}