package net.islandearth.taleofkingdoms.mixin.common;

import net.islandearth.taleofkingdoms.common.event.InventoryInsertCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class InventoryInsertEvent {

    @Shadow @Final
    public PlayerEntity player;

    @Inject(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true)
    public void insertStack(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!InventoryInsertCallback.EVENT.invoker().insertStack(player, slot, stack)) {
            cir.setReturnValue(false);
        }
    }
}
