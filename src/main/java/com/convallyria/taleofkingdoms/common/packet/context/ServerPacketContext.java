package com.convallyria.taleofkingdoms.common.packet.context;

import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;

public record ServerPacketContext(Dist packetEnvironment, Player player, ReentrantBlockableEventLoop taskQueue) implements PacketContext {

}
