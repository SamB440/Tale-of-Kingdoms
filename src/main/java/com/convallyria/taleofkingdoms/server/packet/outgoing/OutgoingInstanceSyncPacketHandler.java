package com.convallyria.taleofkingdoms.server.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.s2c.InstanceSyncPacket;

public final class OutgoingInstanceSyncPacketHandler extends OutServerPacketHandler<InstanceSyncPacket> {

    public OutgoingInstanceSyncPacketHandler() {
        super(Packets.INSTANCE_SYNC, InstanceSyncPacket.CODEC);
    }

}
