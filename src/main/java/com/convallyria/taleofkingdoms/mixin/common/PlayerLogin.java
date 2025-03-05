package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.PlayerJoinCallback;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerLogin {

	@Inject(method = "onPlayerConnect", at = @At("HEAD"))
	private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
		PlayerJoinCallback.EVENT.invoker().onJoin(connection, player);
	}
}
