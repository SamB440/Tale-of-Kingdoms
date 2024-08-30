package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.FixGuildPacket;

public final class OutgoingFixGuildPacketHandler extends OutClientPacketHandler<FixGuildPacket> {

    public OutgoingFixGuildPacketHandler() {
        super(Packets.FIX_GUILD, FixGuildPacket.CODEC);
    }
}
