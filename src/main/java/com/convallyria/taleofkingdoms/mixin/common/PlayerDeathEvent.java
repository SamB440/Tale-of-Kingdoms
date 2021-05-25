package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.EntityDeathCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerDeathEvent {

    @Inject(method = "incrementStat(Lnet/minecraft/util/Identifier;)V", at = @At(value = "INVOKE"))
    private void death(Identifier stat, CallbackInfo ci) {
        if (stat.equals(Stats.DEATHS)) {
            EntityDeathCallback.EVENT.invoker().death(null, ((PlayerEntity) (Object) this));
        }
    }
}
