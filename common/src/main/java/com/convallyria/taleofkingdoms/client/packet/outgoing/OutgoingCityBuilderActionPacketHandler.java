package com.convallyria.taleofkingdoms.client.packet.outgoing;

import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.CityBuilderActionPacket;

public final class OutgoingCityBuilderActionPacketHandler extends OutClientPacketHandler<CityBuilderActionPacket> {

    public OutgoingCityBuilderActionPacketHandler() {
        super(Packets.CITYBUILDER_ACTION, CityBuilderActionPacket.CODEC);
    }
}
