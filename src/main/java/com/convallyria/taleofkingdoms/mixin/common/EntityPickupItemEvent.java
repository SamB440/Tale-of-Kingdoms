package com.convallyria.taleofkingdoms.mixin.common;

import com.convallyria.taleofkingdoms.common.event.EntityPickupItemCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class EntityPickupItemEvent extends Entity {

    @Shadow
    private int pickupDelay;

    @Shadow
    private UUID owner;

    public EntityPickupItemEvent(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (!this.world.isClient) {
            final ItemStack cloned = ((ItemEntity) (Object) this).getStack().copy();
            ItemStack itemStack = ((ItemEntity) (Object) this).getStack();
            Item item = itemStack.getItem();
            int i = itemStack.getCount();
            if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(player.getUuid())) && player.inventory.insertStack(itemStack)) {
                player.sendPickup(this, i);
                if (itemStack.isEmpty()) {
                    this.remove();
                    itemStack.setCount(i);
                }

                player.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), i);
                player.method_29499((ItemEntity) (Object) this);
                EntityPickupItemCallback.EVENT.invoker().pickup(player, cloned);
            }
        }
    }
}
