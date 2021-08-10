package com.convallyria.taleofkingdoms.mixin.server;

import com.convallyria.taleofkingdoms.common.event.GameInstanceCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class GameInstanceEvent {

    @Inject(method = "loadWorld", at = @At("HEAD"))
    protected void onLoadWorld(CallbackInfo ci) {
        GameInstanceCallback.EVENT.invoker().setGameInstance((DedicatedServer) (Object) this);
    }
}
