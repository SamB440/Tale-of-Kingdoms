package com.convallyria.taleofkingdoms.client.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IncomingInstanceSyncPacketHandler extends ClientPacketHandler {

    public IncomingInstanceSyncPacketHandler() {
        super(Packets.INSTANCE_SYNC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, PacketByteBuf attachedData) {
        final ConquestInstance instance = attachedData.decodeAsJson(ConquestInstance.CODEC);
        context.taskQueue().execute(() -> {
            MinecraftClient client = (MinecraftClient) context.taskQueue();
            if (TaleOfKingdoms.CONFIG.mainConfig.developerMode) {
                final String text = "Received sync, " + instance;
                System.out.println(text);
                if (client.player != null) client.player.sendMessage(Text.literal(text));
            }

            final String id = client.getGameProfile().getId().toString();
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            final ConquestInstance existing = api.getConquestInstanceStorage().getConquestInstance(id).orElse(null);
            if (existing != null) {
                existing.uploadData(instance);
            } else {
                api.getConquestInstanceStorage().addConquest(id, instance, true);
            }
        });
    }

    @Override
    public void handleOutgoingPacket(@NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalStateException("Not supported");
    }
}
