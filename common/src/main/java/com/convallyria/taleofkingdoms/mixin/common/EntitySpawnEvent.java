package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.EntitySpawnCallback;
import com.convallyria.taleofkingdoms.common.event.PlayerJoinWorldCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class EntitySpawnEvent {

    @Inject(method = "addFreshEntity", at = @At(value = "HEAD"), cancellable = true)
    public void onSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!EntitySpawnCallback.EVENT.invoker().spawn(entity)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "addPlayer", at = @At("HEAD"))
    private void onAddPlayer(ServerPlayer player, CallbackInfo ci) {
        PlayerJoinWorldCallback.EVENT.invoker().onJoin(player);
    }
}
