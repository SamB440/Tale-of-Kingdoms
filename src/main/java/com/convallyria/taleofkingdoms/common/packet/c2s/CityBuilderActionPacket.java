package com.convallyria.taleofkingdoms.common.packet.c2s;

import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.action.CityBuilderAction;
import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CityBuilderActionPacket(int entityId, CityBuilderAction action, Optional<BuildCosts> costs) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, CityBuilderActionPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CityBuilderActionPacket::entityId,
            ByteBufCodecs.idMapper(i -> CityBuilderAction.values()[i], CityBuilderAction::ordinal), CityBuilderActionPacket::action,
            ByteBufCodecs.optional(ByteBufCodecs.idMapper(i -> BuildCosts.values()[i], BuildCosts::ordinal)), CityBuilderActionPacket::costs,
            CityBuilderActionPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return Packets.CITYBUILDER_ACTION;
    }
}
