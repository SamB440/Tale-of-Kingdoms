package com.convallyria.taleofkingdoms.common.packet.context;

import net.fabricmc.api.EnvType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.thread.ThreadExecutor;

/**
 * Interface defining a context used during packet processing. Allows access
 * to additional information, such as the source/target of the player, or
 * the correct task queue to enqueue synchronization-requiring code on.
 */
public interface PacketContext {
	/**
	 * Get the environment associated with the packet.
	 *
	 * @return EnvType.CLIENT if processing packet on the client side,
	 * EnvType.SERVER otherwise.
	 */
	EnvType packetEnvironment();

	/**
	 * Get the player associated with the packet.
	 *
	 * <p>On the client side, this always returns the client-side player instance.
	 * On the server side, it returns the player belonging to the client this
	 * packet was sent by.
	 *
	 * @return The player associated with the packet.
	 */
	PlayerEntity player();

	/**
	 * Get the task queue for a given side.
	 *
	 * <p>As Minecraft networking I/O is asynchronous, but a lot of its logic is
	 * not thread-safe, it is recommended to do the following:
	 *
	 * <ul><li>read and parse the PacketByteBuf,
	 * <li>run the packet response logic through the main thread task queue via
	 * ThreadTaskQueue.execute(). The method will check if it's not already
	 * on the main thread in order to avoid unnecessary delays, so don't
	 * worry about that!</ul>
	 *
	 * @return The thread task queue.
	 */
	ThreadExecutor taskQueue();
}