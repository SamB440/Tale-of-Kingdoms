package com.convallyria.taleofkingdoms.common.packet.context;

import net.fabricmc.api.EnvType;

public record ServerPacketContext(EnvType packetEnvironment, PlayerEntity player, ThreadExecutor taskQueue) implements PacketContext {

}
