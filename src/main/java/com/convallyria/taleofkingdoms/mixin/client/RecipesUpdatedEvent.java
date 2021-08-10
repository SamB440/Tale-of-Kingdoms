package com.convallyria.taleofkingdoms.mixin.client;

import com.convallyria.taleofkingdoms.common.event.RecipesUpdatedCallback;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class RecipesUpdatedEvent {

    @Inject(method = "onSynchronizeRecipes", at = @At("TAIL"))
    private void onStop(ClientboundUpdateRecipesPacket packet, CallbackInfo ci) {
        RecipesUpdatedCallback.EVENT.invoker().update();
    }
}
