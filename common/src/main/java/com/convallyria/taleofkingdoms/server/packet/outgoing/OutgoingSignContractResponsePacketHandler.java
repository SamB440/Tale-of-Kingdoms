package com.convallyria.taleofkingdoms.server.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.s2c.SignContractResponsePacket;

public final class OutgoingSignContractResponsePacketHandler extends OutServerPacketHandler<SignContractResponsePacket> {

    public OutgoingSignContractResponsePacketHandler() {
        super(Packets.SIGN_CONTRACT_RESPONSE, SignContractResponsePacket.CODEC);
    }
}
