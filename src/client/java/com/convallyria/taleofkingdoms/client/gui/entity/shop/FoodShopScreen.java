package com.convallyria.taleofkingdoms.client.gui.entity.shop;

import com.convallyria.taleofkingdoms.common.entity.guild.FoodShopEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;

public class FoodShopScreen extends DefaultShopScreen {

    public FoodShopScreen(PlayerEntity player, FoodShopEntity entity, ConquestInstance instance) {
        super(player, entity, instance);
    }
}
