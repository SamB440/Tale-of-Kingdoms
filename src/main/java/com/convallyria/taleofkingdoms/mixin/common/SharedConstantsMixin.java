package com.convallyria.taleofkingdoms.mixin.common;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Copied from LazyDFU. Didn't really see the point in depending on it entirely now that you just need this in 1.19.
 */
@Mixin(SharedConstants.class)
public class SharedConstantsMixin {

    /**
     * @author Andrew Steinborn
     * @reason Disables any possibility of enabling DFU "optimizations"
     */
    @Overwrite
    public static void method_43250() {
        // Turn this method into a no-op.
    }
}