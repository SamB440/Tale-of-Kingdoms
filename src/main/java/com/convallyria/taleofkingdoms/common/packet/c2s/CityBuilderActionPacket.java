package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.action.CityBuilderAction;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.Optional;

public record CityBuilderActionPacket(int entityId, CityBuilderAction action, Optional<BuildCosts> costs) implements CustomPayload {

    public static final PacketCodec<RegistryByteBuf, CityBuilderActionPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, CityBuilderActionPacket::entityId,
            PacketCodecs.indexed(i -> CityBuilderAction.values()[i], CityBuilderAction::ordinal), CityBuilderActionPacket::action,
            PacketCodecs.optional(PacketCodecs.indexed(i -> BuildCosts.values()[i], BuildCosts::ordinal)), CityBuilderActionPacket::costs,
            CityBuilderActionPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return Packets.CITYBUILDER_ACTION;
    }
}
