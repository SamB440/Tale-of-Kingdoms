package net.islandearth.taleofkingdoms.mixin;

import net.islandearth.taleofkingdoms.common.event.WorldDeleteCallback;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelStorage.Session.class)
public class WorldDeleteEvent {

    @Final
    @Shadow
    private String directoryName;

    @Inject(method = "deleteSessionLock", at = @At("HEAD"))
    public void onDelete(CallbackInfo ci) {
        WorldDeleteCallback.EVENT.invoker().delete(directoryName);
    }
}
