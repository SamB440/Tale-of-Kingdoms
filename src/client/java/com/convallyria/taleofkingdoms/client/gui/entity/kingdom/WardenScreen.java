package com.convallyria.taleofkingdoms.client.gui.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.kingdom.warden.WardenEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WardenScreen extends BaseUIModelScreen<FlowLayout> {

    private final PlayerEntity player;
    private final WardenEntity entity;
    private final ConquestInstance instance;

    public WardenScreen(PlayerEntity player, WardenEntity entity, ConquestInstance instance) {
        super(FlowLayout.class, BaseUIModelScreen.DataSource.asset(Identifier.of(TaleOfKingdoms.MODID, "warden_ui_model")));
        this.player = player;
        this.entity = entity;
        this.instance = instance;
        player.sendMessage(Text.translatable("entity_type.taleofkingdoms.warden.gui.open"));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        final FlowLayout inner = rootComponent.childById(FlowLayout.class, "inner");

        inner.childById(LabelComponent.class, "total-money").text(Text.translatable("menu.taleofkingdoms.warden.total_money", instance.getPlayer(player).getCoins()));

        inner.childById(ButtonComponent.class, "recruit-knight-button").onPress(b -> {
            entity.buySoldier(player, instance, (byte) 1);
        });

        inner.childById(ButtonComponent.class, "recruit-archer-button").onPress(b -> {
            entity.buySoldier(player, instance, (byte) 2);
//                if (MinecraftClient.getInstance().getServer() == null) {
//                    TaleOfKingdomsClient.getAPI().getClientPacketHandler(Packets.FOREMAN_BUY_WORKER)
//                            .handleOutgoingPacket(player, entity.getId());
//                    return;
//                }
//
//                entity.buyWorker(player, instance);
        });

        inner.childById(ButtonComponent.class, "recall-defenders-button").onPress(b -> {
//                if (MinecraftClient.getInstance().getServer() == null) {
//                    TaleOfKingdomsClient.getAPI().getClientPacketHandler(Packets.FOREMAN_BUY_WORKER)
//                            .handleOutgoingPacket(player, entity.getId());
//                    return;
//                }
//
            entity.recallSoldiers(player, instance);
        });

        inner.childById(ButtonComponent.class, "exit-button").onPress(b -> this.close());
    }

    @Override
    public void close() {
        super.close();
        player.sendMessage(Text.translatable("entity_type.taleofkingdoms.warden.gui.close"));
    }
}
