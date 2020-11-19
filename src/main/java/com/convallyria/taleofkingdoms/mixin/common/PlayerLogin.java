package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.PlayerJoinCallback;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

/**
 *
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */
@Mixin(PlayerManager.class)
public class PlayerLogin {
	@Inject(method = "checkCanJoin", at = @At("HEAD"), cancellable = true)
	private void canJoin(SocketAddress socketAddress, GameProfile gameProfile, CallbackInfoReturnable<Text> cir) {
		Text deny = this.deny(socketAddress, gameProfile);
		if(deny != null)
			cir.setReturnValue(deny);
	}

	@Inject(method = "onPlayerConnect", at = @At("HEAD"))
	private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		PlayerJoinCallback.EVENT.invoker().onJoin(connection, player);
	}

	/**
	 * Return the text the player should be sent if he was denied entry to the server,
	 * this wont allow you to deny players entering their own worlds though.
	 *
	 * if the player is allowed in, return null
	 * @return the kick message for the connecting player
	 */
	@Unique
	private Text deny(SocketAddress address, GameProfile player) {
		//if(new Random().nextBoolean())
			//return new LiteralText("You're not welcome here."); // player not allowed in
		//else
			//return null; // player allowed in
		return null;
	}
}
