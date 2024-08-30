package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.BuildKingdomPacket;

public final class OutgoingBuildKingdomPacket extends OutClientPacketHandler<BuildKingdomPacket> {

    public OutgoingBuildKingdomPacket() {
        super(Packets.BUILD_KINGDOM, BuildKingdomPacket.CODEC);
    }

}
