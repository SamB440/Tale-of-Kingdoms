package net.islandearth.taleofkingdoms.mixin;

import net.islandearth.taleofkingdoms.common.event.RecipesUpdatedCallback;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class RecipesUpdatedEvent {

    @Inject(method = "onSynchronizeRecipes", at = @At("TAIL"))
    private void onStop(SynchronizeRecipesS2CPacket packet, CallbackInfo ci) {
        RecipesUpdatedCallback.EVENT.invoker().update();
    }
}
