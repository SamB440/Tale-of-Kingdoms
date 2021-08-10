package com.convallyria.taleofkingdoms.common.packet.context;

import net.fabricmc.api.EnvType;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.entity.player.Player;

public record ServerPacketContext(EnvType packetEnvironment, Player player, ReentrantBlockableEventLoop taskQueue) implements PacketContext {

}
