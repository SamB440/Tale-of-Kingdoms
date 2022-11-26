package com.convallyria.taleofkingdoms.common.entity;

import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class ShopEntity extends TOKEntity {

    protected ShopEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
    }

    public abstract ImmutableList<ShopItem> getShopItems();

    public abstract ShopParser.GUI getGUIType();
}
