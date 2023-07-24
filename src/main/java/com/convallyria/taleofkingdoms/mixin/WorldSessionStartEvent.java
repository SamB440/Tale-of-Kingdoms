package com.convallyria.taleofkingdoms.mixin;

import com.convallyria.taleofkingdoms.common.event.WorldSessionStartCallback;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelStorage.class)
public class WorldSessionStartEvent {

    @Inject(method = "createSession", at = @At("HEAD"))
    private void createSession(String directoryName, CallbackInfoReturnable<LevelStorage.Session> cir) {
        WorldSessionStartCallback.EVENT.invoker().start(directoryName);
    }

    @Inject(method = "createSessionWithoutSymlinkCheck", at = @At("HEAD"))
    private void createSessionWithoutSymlinkCheck(String directoryName, CallbackInfoReturnable<LevelStorage.Session> cir) {
        WorldSessionStartCallback.EVENT.invoker().start(directoryName);
    }
}
