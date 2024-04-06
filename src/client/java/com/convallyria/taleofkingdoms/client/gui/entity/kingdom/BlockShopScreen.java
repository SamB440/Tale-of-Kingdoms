package com.convallyria.taleofkingdoms.client.gui.entity.kingdom;

import com.convallyria.taleofkingdoms.client.gui.entity.shop.DefaultShopScreen;
import com.convallyria.taleofkingdoms.common.entity.kingdom.BlockShopEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;

public class BlockShopScreen extends DefaultShopScreen {

    public BlockShopScreen(PlayerEntity player, BlockShopEntity entity, ConquestInstance instance) {
        super(player, entity, instance);
    }
}
