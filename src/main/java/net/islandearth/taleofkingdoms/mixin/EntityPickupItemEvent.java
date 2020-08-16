package net.islandearth.taleofkingdoms.mixin;

import net.islandearth.taleofkingdoms.common.event.EntityPickupItemCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class EntityPickupItemEvent {

    @Shadow @Final private DamageTracker damageTracker;

    @Inject(method = "sendPickup", at = @At("HEAD"))
    public void sendPickup(Entity item, int count, CallbackInfo ci) {
        EntityPickupItemCallback.EVENT.invoker().pickup(damageTracker.getEntity(), item, count);
    }
}
