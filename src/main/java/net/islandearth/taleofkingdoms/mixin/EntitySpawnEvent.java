package net.islandearth.taleofkingdoms.mixin;

import net.islandearth.taleofkingdoms.common.event.EntitySpawnCallback;
import net.minecraft.entity.Entity;
import net.minecraft.world.ModifiableWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModifiableWorld.class)
public class EntitySpawnEvent {

    @Inject(method = "spawnEntity", at = @At(value = "INVOKE"), cancellable = true)
    public void onSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!EntitySpawnCallback.EVENT.invoker().spawn(entity)) {
            cir.cancel();
        }
    }
}
