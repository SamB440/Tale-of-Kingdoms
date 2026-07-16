package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.ForemanCollectPacket;

public final class OutgoingForemanCollectPacketHandler extends OutClientPacketHandler<ForemanCollectPacket> {

    public OutgoingForemanCollectPacketHandler() {
        super(Packets.FOREMAN_COLLECT, ForemanCollectPacket.CODEC);
    }

}
