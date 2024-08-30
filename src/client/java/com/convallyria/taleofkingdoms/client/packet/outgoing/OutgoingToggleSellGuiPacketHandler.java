package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.ToggleSellGuiPacket;

public final class OutgoingToggleSellGuiPacketHandler extends OutClientPacketHandler<ToggleSellGuiPacket> {

    public OutgoingToggleSellGuiPacketHandler() {
        super(Packets.TOGGLE_SELL_GUI, ToggleSellGuiPacket.CODEC);
    }

}
