package com.convallyria.taleofkingdoms.mixin.packet;

import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.UpdateStructureBlockC2SPacket;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UpdateStructureBlockC2SPacket.class)
public class ClientUpdateStructureBlockUnlimit {

    @Shadow @Final @Mutable private BlockPos pos;
    @Shadow @Final @Mutable private StructureBlockBlockEntity.Action action;
    @Shadow @Final @Mutable private StructureBlockMode mode;
    @Shadow @Final @Mutable private String structureName;
    @Shadow @Final @Mutable private BlockPos offset;
    @Shadow @Final @Mutable private Vec3i size;
    @Shadow @Final @Mutable private BlockMirror mirror;
    @Shadow @Final @Mutable private BlockRotation rotation;
    @Shadow @Final @Mutable private String metadata;
    @Shadow @Final @Mutable private boolean ignoreEntities;
    @Shadow @Final @Mutable private boolean showAir;
    @Shadow @Final @Mutable private boolean showBoundingBox;
    @Shadow @Final @Mutable private float integrity;
    @Shadow @Final @Mutable private long seed;

    @Inject(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", at = @At("RETURN"))
    private void reinit(PacketByteBuf packetByteBuf, CallbackInfo ci) {
        this.pos = packetByteBuf.readBlockPos();
        this.action = packetByteBuf.readEnumConstant(StructureBlockBlockEntity.Action.class);
        this.mode = packetByteBuf.readEnumConstant(StructureBlockMode.class);
        this.structureName = packetByteBuf.readString();
        this.offset = new BlockPos(MathHelper.clamp(packetByteBuf.readByte(), -512, 512), MathHelper.clamp(packetByteBuf.readByte(), -512, 512), MathHelper.clamp(packetByteBuf.readByte(), -512, 512));
        this.size = new Vec3i(MathHelper.clamp(packetByteBuf.readByte(), 0, 512), MathHelper.clamp(packetByteBuf.readByte(), 0, 512), MathHelper.clamp(packetByteBuf.readByte(), 0, 512));
        this.mirror = packetByteBuf.readEnumConstant(BlockMirror.class);
        this.rotation = packetByteBuf.readEnumConstant(BlockRotation.class);
        this.metadata = packetByteBuf.readString(128);
        this.integrity = MathHelper.clamp(packetByteBuf.readFloat(), 0.0F, 1.0F);
        this.seed = packetByteBuf.readVarLong();
        int k = packetByteBuf.readByte();
        this.ignoreEntities = (k & 1) != 0;
        this.showAir = (k & 2) != 0;
        this.showBoundingBox = (k & 4) != 0;
    }
}
