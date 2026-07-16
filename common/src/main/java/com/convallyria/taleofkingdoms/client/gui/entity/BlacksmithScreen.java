package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.client.gui.entity.shop.DefaultShopScreen;
import com.convallyria.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.world.entity.player.Player;

public class BlacksmithScreen extends DefaultShopScreen {

    public BlacksmithScreen(Player player, BlacksmithEntity entity, ConquestInstance instance) {
        super(player, entity, instance);
    }
}
