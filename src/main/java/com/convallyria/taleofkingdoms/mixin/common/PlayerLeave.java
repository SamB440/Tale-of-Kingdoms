package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.PlayerLeaveCallback;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerLeave {

	@Inject(method = "remove", at = @At("HEAD"))
	private void leave(ServerPlayer player, CallbackInfo ci /*don't even think about it*/) {
		PlayerLeaveCallback.EVENT.invoker().onLeave(player);
	}
}
