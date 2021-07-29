package com.convallyria.taleofkingdoms.mixin.common.quest;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class ObjectiveTestMixin {

    @Inject(method = "tick", at = @At(value = "INVOKE"))
    private void tick(CallbackInfo ci) {
        PlayerEntity playerEntity = ((PlayerEntity) (Object) this);
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            api.getQuests().forEach(quest -> {
                if (quest.isTracked(playerEntity.getUuid())) {
                    quest.getObjectives().forEach(objective -> objective.test(playerEntity));
                }
            });
        });
    }
}
