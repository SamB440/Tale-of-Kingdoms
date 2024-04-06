package com.convallyria.taleofkingdoms.client.mixin;

import com.convallyria.taleofkingdoms.common.event.GameJoinCallback;
import com.convallyria.taleofkingdoms.common.event.RecipesUpdatedCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class NetworkHandlerMixin {

    @Inject(method = "onSynchronizeRecipes", at = @At("TAIL"))
    private void onStop(SynchronizeRecipesS2CPacket packet, CallbackInfo ci) {
        RecipesUpdatedCallback.EVENT.invoker().update();
    }

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        GameJoinCallback.EVENT.invoker().update();
    }
}
