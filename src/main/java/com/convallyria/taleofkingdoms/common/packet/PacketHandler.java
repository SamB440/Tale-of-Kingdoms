package com.convallyria.taleofkingdoms.common.packet;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PacketHandler {

    private final Identifier packet;

    public PacketHandler(Identifier packet) {
        TaleOfKingdoms.LOGGER.info("Registered packet handler [" + this.getClass().getSimpleName() + "]");
        this.packet = packet;
        register();
    }

    public Identifier getPacket() {
        return packet;
    }

    public abstract void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData);

    public abstract void handleOutgoingPacket(Identifier identifier, @NotNull ServerPlayerEntity player, @Nullable ClientConnection connection, @Nullable Object... data);

    protected abstract void register();
}
