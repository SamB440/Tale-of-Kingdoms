package com.convallyria.taleofkingdoms.client.gui.entity.kingdom;

import com.convallyria.taleofkingdoms.client.gui.entity.shop.DefaultShopScreen;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ItemShopEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;

public class ItemShopScreen extends DefaultShopScreen {

    public ItemShopScreen(PlayerEntity player, ItemShopEntity entity, ConquestInstance instance) {
        super(player, entity, instance);
    }
}
