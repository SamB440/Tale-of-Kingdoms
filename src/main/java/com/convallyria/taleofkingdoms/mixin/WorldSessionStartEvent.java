package com.convallyria.taleofkingdoms.mixin;

import com.convallyria.taleofkingdoms.common.event.WorldSessionStartCallback;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelStorageSource.class)
public class WorldSessionStartEvent {

    @Inject(method = "createSession", at = @At("HEAD"))
    private void onStop(String directoryName, CallbackInfoReturnable<LevelStorageSource.LevelStorageAccess> cir) {
        WorldSessionStartCallback.EVENT.invoker().start(directoryName);
    }
}
