package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.ForemanBuyWorkerPacket;

public final class OutgoingForemanBuyWorkerPacketHandler extends OutClientPacketHandler<ForemanBuyWorkerPacket> {

    public OutgoingForemanBuyWorkerPacketHandler() {
        super(Packets.FOREMAN_BUY_WORKER, ForemanBuyWorkerPacket.CODEC);
    }

}
