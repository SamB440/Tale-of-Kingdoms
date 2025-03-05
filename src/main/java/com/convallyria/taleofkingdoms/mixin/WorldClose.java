package com.convallyria.taleofkingdoms.mixin;

import com.convallyria.taleofkingdoms.common.event.WorldStopCallback;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class WorldClose {

	@Inject(method = "stop", at = @At("RETURN"))
	private void onStop(boolean stopImmediately, CallbackInfo ci) {
		WorldStopCallback.EVENT.invoker().stop();
	}

}
