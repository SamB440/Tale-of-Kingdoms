package com.convallyria.taleofkingdoms.server.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.s2c.OpenScreenPacket;

public final class OutgoingOpenScreenPacketHandler extends OutServerPacketHandler<OpenScreenPacket> {

    public OutgoingOpenScreenPacketHandler() {
        super(Packets.OPEN_CLIENT_SCREEN, OpenScreenPacket.CODEC);
    }
}
