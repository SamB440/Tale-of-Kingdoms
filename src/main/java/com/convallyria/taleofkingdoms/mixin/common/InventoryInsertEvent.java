package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.InventoryInsertCallback;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class InventoryInsertEvent {

    @Shadow @Final
    public Player player;

    @Inject(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true)
    public void insertStack(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!InventoryInsertCallback.EVENT.invoker().insertStack(player, slot, stack)) {
            cir.setReturnValue(false);
        }
    }
}
