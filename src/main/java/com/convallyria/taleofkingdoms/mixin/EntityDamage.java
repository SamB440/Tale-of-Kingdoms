package com.convallyria.taleofkingdoms.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * This is free and unencumbered software released into the public domain.
 * <p>
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * <p>
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * <p>
 * For more information, please refer to <http://unlicense.org/>
 */
@Mixin ({
        // @formatter:off
            Entity.class,
            HangingEntity.class,
            AbstractMinecart.class,
            Animal.class,
            ArmorStand.class,
            Boat.class,
            EndCrystal.class,
            EnderDragon.class,
            ExperienceOrb.class,
            AbstractHurtingProjectile.class,
            ItemEntity.class,
            ShulkerBullet.class,
        // @formatter:on
        })
public class EntityDamage {
	@ModifyVariable (method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
	                 at = @At ("HEAD"),
	                 argsOnly = true,
	                 index = 2
	)
	private float damage(float a, DamageSource source, float amount) {
		return this.handleDamage(source, amount);
	}

	/**
	 * this event does not guarantee any side effects cant happen,
	 * so for example, animals will still loose their love effect
	 * @param source the source of the damage
	 * @param damage the amount of damage
	 * @return the new amount of damage
	 */
	@Unique
	private float handleDamage(DamageSource source, float damage) {
		return damage*.5f;
	}
}
