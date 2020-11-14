package net.islandearth.taleofkingdoms.mixin.server;

import net.islandearth.taleofkingdoms.common.event.GameInstanceCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class GameInstanceEvent {

    @Inject(method = "loadWorld", at = @At("HEAD"), remap = false)
    public void onSetGameInstance(CallbackInfo ci) {
        GameInstanceCallback.EVENT.invoker().setGameInstance((MinecraftDedicatedServer) (Object) this);
    }
}
