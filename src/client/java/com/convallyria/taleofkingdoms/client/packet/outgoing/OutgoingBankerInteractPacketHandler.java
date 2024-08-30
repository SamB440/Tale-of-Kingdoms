package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.BankerInteractPacket;

public final class OutgoingBankerInteractPacketHandler extends OutClientPacketHandler<BankerInteractPacket> {

    public OutgoingBankerInteractPacketHandler() {
        super(Packets.BANKER_INTERACT, BankerInteractPacket.CODEC);
    }

}
