package com.convallyria.taleofkingdoms.common.packet.context;

import net.fabricmc.api.EnvType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.thread.ThreadExecutor;

public record ClientPacketContext(EnvType packetEnvironment, PlayerEntity player, ThreadExecutor<?> taskQueue) implements PacketContext {

}
