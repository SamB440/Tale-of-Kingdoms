package com.convallyria.taleofkingdoms.mixin.structure;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.StructureBlockBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(StructureBlockBlockEntityRenderer.class)
public class StructureBlockRenderMixin {

    /**
     * @reason Increase the distance that the bounding box can be seen up to 256 blocks
     * @author SamB440/Cotander
     */
    @Overwrite
    @Environment(EnvType.CLIENT)
    public int getRenderDistance() {
        return 256;
    }
}
