package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.SignContractPacket;

public final class OutgoingSignContractPacketHandler extends OutClientPacketHandler<SignContractPacket> {

    public OutgoingSignContractPacketHandler() {
        super(Packets.SIGN_CONTRACT, SignContractPacket.CODEC);
    }
}
