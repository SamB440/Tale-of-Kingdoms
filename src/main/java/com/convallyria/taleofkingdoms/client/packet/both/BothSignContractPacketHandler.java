package com.convallyria.taleofkingdoms.client.packet.both;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BothSignContractPacketHandler extends ClientPacketHandler {

    public BothSignContractPacketHandler() {
        super(TaleOfKingdoms.SIGN_CONTRACT_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        boolean sign = attachedData.readBoolean();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
            instance.setHasContract(sign);
            if (sign) Translations.GUILDMASTER_CONTRACT_SIGN.send(context.player());
        }));
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        if (data != null && data[0] instanceof Boolean) {
            boolean sign = (Boolean) data[0];
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeBoolean(sign);
            sendPacket(player, passedData);
        }
    }
}
