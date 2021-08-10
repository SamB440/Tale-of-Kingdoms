package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.EntityDeathCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerDeathEvent {

    @Inject(method = "incrementStat(Lnet/minecraft/util/Identifier;)V", at = @At(value = "INVOKE"))
    private void death(ResourceLocation stat, CallbackInfo ci) {
        if (stat.equals(Stats.DEATHS)) {
            EntityDeathCallback.EVENT.invoker().death(null, ((Player) (Object) this));
        }
    }
}
