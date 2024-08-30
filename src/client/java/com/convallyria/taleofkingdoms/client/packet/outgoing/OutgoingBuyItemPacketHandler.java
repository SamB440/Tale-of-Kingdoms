package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.BuyItemPacket;

public final class OutgoingBuyItemPacketHandler extends OutClientPacketHandler<BuyItemPacket> {

    public OutgoingBuyItemPacketHandler() {
        super(Packets.BUY_ITEM, BuyItemPacket.CODEC);
    }

}
