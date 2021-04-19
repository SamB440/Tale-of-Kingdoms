package com.convallyria.taleofkingdoms.common.schematic.blocky;

import com.convallyria.taleofkingdoms.common.schematic.blocky.block.NBTBlock;
import com.convallyria.taleofkingdoms.common.schematic.blocky.block.NBTChestBlock;
import com.convallyria.taleofkingdoms.common.schematic.blocky.block.NBTSignBlock;
import net.minecraft.block.Material;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public enum NBTMaterial {
    SIGN(true, NBTSignBlock.class),
    CHEST(false, NBTChestBlock.class),
    TRAPPED_CHEST(false, NBTChestBlock.class), // does this exist in nbt?

    OAK_SIGN(true, NBTSignBlock.class),
    SPRUCE_SIGN(true, NBTSignBlock.class),
    BIRCH_SIGN(true, NBTSignBlock.class),
    JUNGLE_SIGN(true, NBTSignBlock.class),
    ACACIA_SIGN(true, NBTSignBlock.class),
    DARK_OAK_SIGN(true, NBTSignBlock.class),
    CRIMSON_SIGN(true, NBTSignBlock.class),
    WARPED_SIGN(true, NBTSignBlock.class),
    OAK_WALL_SIGN(true, NBTSignBlock.class),
    SPRUCE_WALL_SIGN(true, NBTSignBlock.class),
    BIRCH_WALL_SIGN(true, NBTSignBlock.class),
    JUNGLE_WALL_SIGN(true, NBTSignBlock.class),
    ACACIA_WALL_SIGN(true, NBTSignBlock.class),
    DARK_OAK_WALL_SIGN(true, NBTSignBlock.class),
    CRIMSON_WALL_SIGN(true, NBTSignBlock.class),
    WARPED_WALL_SIGN(true, NBTSignBlock.class),

    LAVA(true),
    VINE(true),
    WATER(true),
    ARMOR_STAND(true),
    BLACK_BANNER(true),
    BLACK_WALL_BANNER(true),
    BLUE_BANNER(true),
    BLUE_WALL_BANNER(true),
    BROWN_BANNER(true),
    BROWN_WALL_BANNER(true),
    CYAN_BANNER(true),
    CYAN_WALL_BANNER(true),
    GRAY_BANNER(true),
    GRAY_WALL_BANNER(true),
    GREEN_BANNER(true),
    GREEN_WALL_BANNER(true),
    LIGHT_BLUE_BANNER(true),
    LIGHT_BLUE_WALL_BANNER(true),
    LIGHT_GRAY_BANNER(true),
    LIGHT_GRAY_WALL_BANNER(true),
    LIME_BANNER(true),
    LIME_WALL_BANNER(true),
    MAGENTA_BANNER(true),
    MAGENTA_WALL_BANNER(true),
    ORANGE_BANNER(true),
    ORANGE_WALL_BANNER(true),
    PINK_BANNER(true),
    PINK_WALL_BANNER(true),
    PURPLE_BANNER(true),
    PURPLE_WALL_BANNER(true),
    RED_BANNER(true),
    RED_WALL_BANNER(true),
    WHITE_BANNER(true),
    WHITE_WALL_BANNER(true),
    YELLOW_BANNER(true),
    YELLOW_WALL_BANNER(true),

    GRASS(true),
    TALL_GRASS(true),
    SEAGRASS(true),
    TALL_SEAGRASS(true),
    FLOWER_POT(true),
    SUNFLOWER(true),
    CHORUS_FLOWER(true),
    OXEYE_DAISY(true),
    DEAD_BUSH(true),
    FERN(true),
    DANDELION(true),
    POPPY(true),
    BLUE_ORCHID(true),
    ALLIUM(true),
    AZURE_BLUET(true),
    RED_TULIP(true),
    ORANGE_TULIP(true),
    WHITE_TULIP(true),
    PINK_TULIP(true),
    BROWN_MUSHROOM(true),
    RED_MUSHROOM(true),
    END_ROD(true),
    ROSE_BUSH(true),
    PEONY(true),
    LARGE_FERN(true),
    REDSTONE(true),
    REPEATER(true),
    COMPARATOR(true),
    LEVER(true),
    SEA_PICKLE(true),
    SUGAR_CANE(true),
    FIRE(true),
    WHEAT(true),
    WHEAT_SEEDS(true),
    CARROTS(true),
    BEETROOT(true),
    BEETROOT_SEEDS(true),
    MELON(true),
    MELON_STEM(true),
    MELON_SEEDS(true),
    POTATOES(true),
    PUMPKIN(true),
    PUMPKIN_STEM(true),
    PUMPKIN_SEEDS(true),
    TORCH(true),
    RAIL(true),
    ACTIVATOR_RAIL(true),
    DETECTOR_RAIL(true),
    POWERED_RAIL(true),

    ACACIA_FENCE(true),
    ACACIA_FENCE_GATE(true),
    BIRCH_FENCE(true),
    BIRCH_FENCE_GATE(true),
    DARK_OAK_FENCE(true),
    DARK_OAK_FENCE_GATE(true),
    JUNGLE_FENCE(true),
    JUNGLE_FENCE_GATE(true),
    NETHER_BRICK_FENCE(true),
    OAK_FENCE(true),
    OAK_FENCE_GATE(true),
    SPRUCE_FENCE(true),
    SPRUCE_FENCE_GATE(true),

    OAK_DOOR(true),
    ACACIA_DOOR(true),
    BIRCH_DOOR(true),
    DARK_OAK_DOOR(true),
    JUNGLE_DOOR(true),
    SPRUCE_DOOR(true),
    IRON_DOOR(true);

    private final boolean delayed;
    private final Class<? extends NBTBlock> nbtBlock;

    NBTMaterial(boolean delayed) {
        this(delayed, null);
    }

    NBTMaterial(boolean delayed, Class<? extends NBTBlock> nbtBlock) {
        this.delayed = delayed;
        this.nbtBlock = nbtBlock;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public NBTBlock getNbtBlock(CompoundTag tagCompound) throws ReflectiveOperationException {
        return nbtBlock.getConstructor(tagCompound.getClass()).newInstance(tagCompound);
    }

    @Nullable
    public static NBTMaterial fromTag(CompoundTag nbtTagCompound) {
        try {
            return NBTMaterial.valueOf(nbtTagCompound.getString("Id").
                    replace("minecraft:", "").
                    toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    @Nullable
    public static NBTMaterial fromMinecraft(Material material) {
        try {
            return NBTMaterial.valueOf(material.toString().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
