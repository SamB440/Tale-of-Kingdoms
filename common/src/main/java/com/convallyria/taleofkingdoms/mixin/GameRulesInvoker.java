package com.convallyria.taleofkingdoms.mixin;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * {@link GameRules#register} is package-private; this exposes it so the common module can
 * register game rules on both loaders without depending on Fabric API's GameRuleRegistry.
 */
@Mixin(GameRules.class)
public interface GameRulesInvoker {

    @Invoker("register")
    static <T extends GameRules.Value<T>> GameRules.Key<T> callRegister(String name, GameRules.Category category, GameRules.Type<T> type) {
        throw new AssertionError();
    }
}
