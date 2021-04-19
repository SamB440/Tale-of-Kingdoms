package com.convallyria.taleofkingdoms.common.schematic.blocky.block;

import com.convallyria.taleofkingdoms.common.schematic.blocky.WrongIdException;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class NBTBlock {
    
    private final CompoundTag nbtTag;
    
    public NBTBlock(CompoundTag nbtTag) {
        this.nbtTag = nbtTag;
    }

    public CompoundTag getNbtTag() {
        return nbtTag;
    }

    public Vec3d getOffset() {
        CompoundTag compound = this.getNbtTag();
        int[] pos = compound.getIntArray("Pos");
        return new Vec3d(pos[0], pos[1], pos[2]);
    }

    public abstract void setData(ServerWorld world, BlockPos pos, BlockState block) throws WrongIdException;

    public abstract boolean isEmpty();
}
