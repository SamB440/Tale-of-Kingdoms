package com.convallyria.taleofkingdoms.client.mixin;

import com.convallyria.taleofkingdoms.common.event.GameJoinCallback;
import com.convallyria.taleofkingdoms.common.event.RecipesUpdatedCallback;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class NetworkHandlerMixin {

    @Inject(method = "handleUpdateRecipes", at = @At("TAIL"))
    private void onStop(ClientboundUpdateRecipesPacket packet, CallbackInfo ci) {
        RecipesUpdatedCallback.EVENT.invoker().update();
    }

    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void onJoin(ClientboundLoginPacket packet, CallbackInfo ci) {
        GameJoinCallback.EVENT.invoker().update();
    }
}
