package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.HireHunterPacket;

public final class OutgoingHunterPacketHandler extends OutClientPacketHandler<HireHunterPacket> {

    public OutgoingHunterPacketHandler() {
        super(Packets.HIRE_HUNTER, HireHunterPacket.CODEC);
    }

}
