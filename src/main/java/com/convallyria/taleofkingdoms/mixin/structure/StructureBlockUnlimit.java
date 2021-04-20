package com.convallyria.taleofkingdoms.mixin.structure;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(StructureBlockBlockEntity.class)
public class StructureBlockUnlimit {
    
    @Shadow
    private StructureBlockMode mode;
    
    @Shadow
    private BlockPos offset;
    
    @Shadow
    private BlockPos size;
    
    @Shadow
    private BlockMirror mirror;
    
    @Shadow
    private String author;
    
    @Shadow
    private String metadata;
    
    @Shadow
    private BlockRotation rotation;
    
    @Shadow
    private long seed;
    
    @Shadow
    private boolean ignoreEntities;
    
    @Shadow
    private boolean powered;
    
    @Shadow
    private boolean showAir;
    
    @Shadow
    private boolean showBoundingBox;
    
    @Shadow
    private float integrity;

    /**
     * @reason Increase the distance that the bounding box can be seen
     * @author SamB440/Cotander
     */
    @Overwrite
    @Environment(EnvType.CLIENT)
    public double getSquaredRenderDistance() {
        return 256.0D;
    }

    /**
     * @reason Increases structure block max size
     * @author SamB440/Cotander
     */
    @Overwrite
    public void fromTag(BlockState state, CompoundTag tag) {
        StructureBlockBlockEntity entity = (StructureBlockBlockEntity) (Object) this;
        StructureBlockAccessor accessor = (StructureBlockAccessor) entity;
        entity.setPos(new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")));
        entity.setStructureName(tag.getString("name"));
        this.author = tag.getString("author");
        this.metadata = tag.getString("metadata");
        int i = MathHelper.clamp(tag.getInt("posX"), -512, 512);
        int j = MathHelper.clamp(tag.getInt("posY"), -512, 512);
        int k = MathHelper.clamp(tag.getInt("posZ"), -512, 512);
        this.offset = new BlockPos(i, j, k);
        int l = MathHelper.clamp(tag.getInt("sizeX"), 0, 512);
        int m = MathHelper.clamp(tag.getInt("sizeY"), 0, 512);
        int n = MathHelper.clamp(tag.getInt("sizeZ"), 0, 512);
        this.size = new BlockPos(l, m, n);
        
        try {
            this.rotation = BlockRotation.valueOf(tag.getString("rotation"));
        } catch (IllegalArgumentException var12) {
            this.rotation = BlockRotation.NONE;
        }
        
        try {
            this.mirror = BlockMirror.valueOf(tag.getString("mirror"));
        } catch (IllegalArgumentException var11) {
            this.mirror = BlockMirror.NONE;
        }
        
        try {
            this.mode = StructureBlockMode.valueOf(tag.getString("mode"));
        } catch (IllegalArgumentException var10) {
            this.mode = StructureBlockMode.DATA;
        }
        
        this.ignoreEntities = tag.getBoolean("ignoreEntities");
        this.powered = tag.getBoolean("powered");
        this.showAir = tag.getBoolean("showair");
        this.showBoundingBox = tag.getBoolean("showboundingbox");
        if (tag.contains("integrity")) {
            this.integrity = tag.getFloat("integrity");
        } else {
            this.integrity = 1.0F;
        }
        
        this.seed = tag.getLong("seed");
        accessor.updateBlockMode();
    }
    
    /**
     * @reason Increases structure block detection size.
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
            BlockPos blockPos2 = new BlockPos(blockPos.getX() - 240, 0, blockPos.getZ() - 240);
            BlockPos blockPos3 = new BlockPos(blockPos.getX() + 240, 255, blockPos.getZ() + 240);
            List<StructureBlockBlockEntity> list = accessor.findStructureBlockEntities(blockPos2, blockPos3);
            List<StructureBlockBlockEntity> list2 = accessor.findCorners(list);
            if (list2.size() < 1) {
                return false;
            } else {
                BlockBox blockBox = accessor.makeBoundingBox(blockPos, list2);
                if (blockBox.maxX - blockBox.minX > 1 && blockBox.maxY - blockBox.minY > 1 && blockBox.maxZ - blockBox.minZ > 1) {
                    this.offset = new BlockPos(blockBox.minX - blockPos.getX() + 1, blockBox.minY - blockPos.getY() + 1, blockBox.minZ - blockPos.getZ() + 1);
                    this.size = new BlockPos(blockBox.maxX - blockBox.minX - 1, blockBox.maxY - blockBox.minY - 1, blockBox.maxZ - blockBox.minZ - 1);
                    entity.markDirty();
                    BlockState blockState = entity.getWorld().getBlockState(blockPos);
                    entity.getWorld().updateListeners(blockPos, blockState, blockState, 3);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
