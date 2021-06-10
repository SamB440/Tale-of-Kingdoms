package com.convallyria.taleofkingdoms.mixin.structure;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Mixin(StructureBlockBlockEntity.class)
public class StructureBlockUnlimit {

    @Shadow private StructureBlockMode mode;
    @Shadow private BlockPos offset;
    @Shadow private Vec3i size;
    @Shadow private BlockMirror mirror;
    @Shadow private String author;
    @Shadow private String metadata;
    @Shadow private BlockRotation rotation;
    @Shadow private long seed;
    @Shadow private boolean ignoreEntities;
    @Shadow private boolean powered;
    @Shadow private boolean showAir;
    @Shadow private boolean showBoundingBox;
    @Shadow private float integrity;

    /**
     * @reason Increases structure block max size to 512
     * @author SamB440/Cotander
     */
    @Overwrite
    public void readNbt(NbtCompound nbtCompound) {
        StructureBlockBlockEntity entity = (StructureBlockBlockEntity) (Object) this;
        StructureBlockAccessor accessor = (StructureBlockAccessor) entity;
        // super.readNbt(nbtCompound);
        entity.setStructureName(nbtCompound.getString("name"));
        this.author = nbtCompound.getString("author");
        this.metadata = nbtCompound.getString("metadata");
        int i = MathHelper.clamp(nbtCompound.getInt("posX"), -512, 512);
        int j = MathHelper.clamp(nbtCompound.getInt("posY"), -512, 512);
        int k = MathHelper.clamp(nbtCompound.getInt("posZ"), -512, 512);
        this.offset = new BlockPos(i, j, k);
        int l = MathHelper.clamp(nbtCompound.getInt("sizeX"), 0, 512);
        int m = MathHelper.clamp(nbtCompound.getInt("sizeY"), 0, 512);
        int n = MathHelper.clamp(nbtCompound.getInt("sizeZ"), 0, 512);
        this.size = new Vec3i(l, m, n);

        try {
            this.rotation = BlockRotation.valueOf(nbtCompound.getString("rotation"));
        } catch (IllegalArgumentException var11) {
            this.rotation = BlockRotation.NONE;
        }

        try {
            this.mirror = BlockMirror.valueOf(nbtCompound.getString("mirror"));
        } catch (IllegalArgumentException var10) {
            this.mirror = BlockMirror.NONE;
        }

        try {
            this.mode = StructureBlockMode.valueOf(nbtCompound.getString("mode"));
        } catch (IllegalArgumentException var9) {
            this.mode = StructureBlockMode.DATA;
        }

        this.ignoreEntities = nbtCompound.getBoolean("ignoreEntities");
        this.powered = nbtCompound.getBoolean("powered");
        this.showAir = nbtCompound.getBoolean("showair");
        this.showBoundingBox = nbtCompound.getBoolean("showboundingbox");
        if (nbtCompound.contains("integrity")) {
            this.integrity = nbtCompound.getFloat("integrity");
        } else {
            this.integrity = 1.0F;
        }

        this.seed = nbtCompound.getLong("seed");
        accessor.updateBlockMode();
    }
    
    /**
     * @reason Increases structure block detection size up to 255
     * @author SamB440/Cotander
     */
    @Overwrite
    public boolean detectStructureSize() {
        StructureBlockBlockEntity entity = (StructureBlockBlockEntity) (Object) this;
        StructureBlockAccessor accessor = (StructureBlockAccessor) entity;
        if (this.mode != StructureBlockMode.SAVE) {
            return false;
        } else {
            BlockPos blockPos = entity.getPos();
            BlockPos blockPos2 = new BlockPos(blockPos.getX() - 80, entity.getWorld().getBottomY(), blockPos.getZ() - 80);
            BlockPos blockPos3 = new BlockPos(blockPos.getX() + 80, entity.getWorld().getTopY() - 1, blockPos.getZ() + 80);
            Stream<BlockPos> stream = accessor.streamCornerPos(blockPos2, blockPos3);
            return getStructureBox(blockPos, stream).filter((blockBox) -> {
                int i = blockBox.getMaxX() - blockBox.getMinX();
                int j = blockBox.getMaxY() - blockBox.getMinY();
                int k = blockBox.getMaxZ() - blockBox.getMinZ();
                if (i > 1 && j > 1 && k > 1) {
                    this.offset = new BlockPos(blockBox.getMinX() - blockPos.getX() + 1, blockBox.getMinY() - blockPos.getY() + 1, blockBox.getMinZ() - blockPos.getZ() + 1);
                    this.size = new Vec3i(i - 1, j - 1, k - 1);
                    entity.markDirty();
                    BlockState blockState = entity.getWorld().getBlockState(blockPos);
                    entity.getWorld().updateListeners(blockPos, blockState, blockState, 3);
                    return true;
                } else {
                    return false;
                }
            }).isPresent();
        }
    }

    /**
     * @reason Provides access to this method in Java 16.
     * @author SamB440
     */
    @Overwrite
    private static Optional<BlockBox> getStructureBox(BlockPos blockPos, Stream<BlockPos> stream) {
        Iterator<BlockPos> iterator = stream.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        } else {
            BlockPos blockPos2 = iterator.next();
            BlockBox blockBox = new BlockBox(blockPos2);
            if (iterator.hasNext()) {
                Objects.requireNonNull(blockBox);
                iterator.forEachRemaining(blockBox::encompass);
            } else {
                blockBox.encompass(blockPos);
            }

            return Optional.of(blockBox);
        }
    }
}
