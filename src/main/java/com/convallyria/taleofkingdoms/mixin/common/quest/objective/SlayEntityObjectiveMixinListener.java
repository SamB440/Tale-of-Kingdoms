package com.convallyria.taleofkingdoms.mixin.common.quest.objective;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.quest.objective.SlayEntityObjective;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class SlayEntityObjectiveMixinListener {

    @Inject(method = "onDeath",
            at = @At("HEAD"))
    public void interact(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.getAttacker() instanceof PlayerEntity playerEntity) {
            TaleOfKingdoms.getAPI().ifPresent(api -> api.getQuests().forEach(quest -> {
                if (!quest.isTracked(playerEntity.getUuid())) return;
                quest.getObjectives().forEach(objective -> {
                    if (!quest.isCurrentPriority(playerEntity, objective)) return;
                    if (objective instanceof SlayEntityObjective slay) {
                        LivingEntity entity = (LivingEntity) (Object) this;
                        if (entity.getClass().isAssignableFrom(slay.getEntity())) {
                            objective.test(playerEntity);
                        }
                    }
                });
            }));
        }
    }
}
