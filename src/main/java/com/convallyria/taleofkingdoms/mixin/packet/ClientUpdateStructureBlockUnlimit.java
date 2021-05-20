package com.convallyria.taleofkingdoms.mixin.packet;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.UpdateStructureBlockC2SPacket;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(UpdateStructureBlockC2SPacket.class)
public class ClientUpdateStructureBlockUnlimit {

    @Shadow private BlockPos pos;
    @Shadow private StructureBlockBlockEntity.Action action;
    @Shadow private StructureBlockMode mode;
    @Shadow private String structureName;
    @Shadow private BlockPos offset;
    @Shadow private BlockPos size;
    @Shadow private BlockMirror mirror;
    @Shadow private BlockRotation rotation;
    @Shadow private String metadata;
    @Shadow private boolean ignoreEntities;
    @Shadow private boolean showAir;
    @Shadow private boolean showBoundingBox;
    @Shadow private float integrity;
    @Shadow private long seed;

    /**
     * @reason Increase the distance that the bounding box can be seen up to 256 blocks
     * @author SamB440/Cotander
     */
    @Overwrite
    public void read(PacketByteBuf buf) throws IOException {
        this.pos = buf.readBlockPos();
        this.action = buf.readEnumConstant(StructureBlockBlockEntity.Action.class);
        this.mode = buf.readEnumConstant(StructureBlockMode.class);
        this.structureName = buf.readString(32767);
        this.offset = new BlockPos(MathHelper.clamp(buf.readByte(), -512, 512), MathHelper.clamp(buf.readByte(), -512, 512), MathHelper.clamp(buf.readByte(), -512, 512));
        this.size = new BlockPos(MathHelper.clamp(buf.readByte(), 0, 512), MathHelper.clamp(buf.readByte(), 0, 512), MathHelper.clamp(buf.readByte(), 0, 512));
        this.mirror = buf.readEnumConstant(BlockMirror.class);
        this.rotation = buf.readEnumConstant(BlockRotation.class);
        this.metadata = buf.readString(12);
        this.integrity = MathHelper.clamp(buf.readFloat(), 0.0F, 1.0F);
        this.seed = buf.readVarLong();
        int k = buf.readByte();
        this.ignoreEntities = (k & 1) != 0;
        this.showAir = (k & 2) != 0;
        this.showBoundingBox = (k & 4) != 0;
    }
}
