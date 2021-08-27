package com.convallyria.taleofkingdoms.mixin.common.quest.objective;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.quest.objective.SpeakToNPCObjective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class SpeakToNPCObjectiveMixinListener {

    @Inject(method = "interact",
            at = @At("HEAD"))
    public void interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (hand != Hand.MAIN_HAND) return;
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        TaleOfKingdoms.getAPI().ifPresent(api -> api.getQuests().forEach(quest -> {
            if (!quest.isTracked(playerEntity.getUuid())) return;
            quest.getObjectives().forEach(objective -> {
                if (!quest.isCurrentPriority(playerEntity, objective)) return;
                if (objective instanceof SpeakToNPCObjective speak) {
                    if (entity.getClass().isAssignableFrom(speak.getNpc())) {
                        objective.test(playerEntity);
                    }
                }
            });
        }));
    }
}
