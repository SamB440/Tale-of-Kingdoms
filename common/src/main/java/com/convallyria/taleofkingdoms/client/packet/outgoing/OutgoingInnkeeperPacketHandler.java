package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.InnkeeperActionPacket;

public final class OutgoingInnkeeperPacketHandler extends OutClientPacketHandler<InnkeeperActionPacket> {

    public OutgoingInnkeeperPacketHandler() {
        super(Packets.INNKEEPER_HIRE_ROOM, InnkeeperActionPacket.CODEC);
    }

}
