package com.convallyria.taleofkingdoms.mixin;

import com.convallyria.taleofkingdoms.common.event.WorldSessionStartCallback;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelStorageSource.class)
public class WorldSessionStartEvent {

    @Inject(method = "validateAndCreateAccess", at = @At("HEAD"))
    private void createSession(String directoryName, CallbackInfoReturnable<LevelStorageSource.LevelStorageAccess> cir) {
        WorldSessionStartCallback.EVENT.invoker().start(directoryName);
    }

    @Inject(method = "createAccess", at = @At("HEAD"))
    private void createSessionWithoutSymlinkCheck(String directoryName, CallbackInfoReturnable<LevelStorageSource.LevelStorageAccess> cir) {
        WorldSessionStartCallback.EVENT.invoker().start(directoryName);
    }
}
