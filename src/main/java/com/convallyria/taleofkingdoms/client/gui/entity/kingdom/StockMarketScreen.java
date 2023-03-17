package com.convallyria.taleofkingdoms.client.gui.entity.kingdom;

import com.convallyria.taleofkingdoms.client.gui.entity.shop.DefaultShopScreen;
import com.convallyria.taleofkingdoms.common.entity.kingdom.StockMarketEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;

public class StockMarketScreen extends DefaultShopScreen {

    public StockMarketScreen(PlayerEntity player, StockMarketEntity entity, ConquestInstance instance) {
        super(player, entity, instance);
    }
}
