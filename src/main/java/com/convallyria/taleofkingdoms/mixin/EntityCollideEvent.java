package com.convallyria.taleofkingdoms.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright LAW.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */
@Mixin (Entity.class)
public class EntityCollideEvent {
	@Inject (method = "checkBlockCollision",
	         at = @At (value = "INVOKE",
	                   target = "Lnet/minecraft/block/BlockState;onEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V"),
	         cancellable = true,
	         locals = LocalCapture.CAPTURE_FAILHARD)
	private void collide(CallbackInfo ci, AABB box, BlockPos.MutableBlockPos pooledMutable, BlockPos.MutableBlockPos pooledMutable2, BlockPos.MutableBlockPos pooledMutable3, int x, int y, int z, BlockState blockState) {
		this.onCollide(blockState, x, y, z);
	}

	/**
	 * called when an entity collides with / enters a block, so for example when an entity moves, it moves into some air, this method is invoked for that too
	 * be careful, this can be called literally millions of times per second, so every millisecond counts!
	 *
	 * @param state the blockstate of the block being collided with
	 * @param x the x coordinate of the block
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	@Unique
	private void onCollide(BlockState state, int x, int y, int z) {

	}
}
