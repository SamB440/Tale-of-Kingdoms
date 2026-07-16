package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.ChangeTitlePacket;

public final class OutgoingChangeTitlePacketHandler extends OutClientPacketHandler<ChangeTitlePacket> {

    public OutgoingChangeTitlePacketHandler() {
        super(Packets.CHANGE_TITLE, ChangeTitlePacket.CODEC);
    }
}
