package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.EntityPickupItemCallback;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class EntityPickupItemEvent {

    @Inject(method = "onItemPickup", at = @At(value = "INVOKE"))
    public void onPickup(ItemEntity itemEntity, CallbackInfo ci) {
        //noinspection ConstantConditions - Mixin, injected into LivingEntity which can be instanceof PlayerEntity.
        if ((Object) this instanceof Player) {
            EntityPickupItemCallback.EVENT.invoker().pickup(((Player) (Object) this), itemEntity.getItem().copy());
        }
    }
}
