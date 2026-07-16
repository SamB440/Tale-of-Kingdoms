package com.convallyria.taleofkingdoms.common.entity.reficule;

import net.minecraft.world.entity.Entity;

public interface TeleportAbility {

    boolean teleportTo(Entity entity);

    default boolean spreadFire() {
        return false;
    }
}
