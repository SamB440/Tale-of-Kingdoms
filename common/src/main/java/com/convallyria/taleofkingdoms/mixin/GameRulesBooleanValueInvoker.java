package com.convallyria.taleofkingdoms.mixin;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * {@link GameRules.BooleanValue}'s factory is package-private; see {@link GameRulesInvoker}.
 */
@Mixin(GameRules.BooleanValue.class)
public interface GameRulesBooleanValueInvoker {

    @Invoker("create")
    static GameRules.Type<GameRules.BooleanValue> callCreate(boolean defaultValue) {
        throw new AssertionError();
    }
}
