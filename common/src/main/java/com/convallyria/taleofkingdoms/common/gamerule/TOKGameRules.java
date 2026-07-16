package com.convallyria.taleofkingdoms.common.gamerule;

import com.convallyria.taleofkingdoms.mixin.GameRulesBooleanValueInvoker;
import com.convallyria.taleofkingdoms.mixin.GameRulesInvoker;
import net.minecraft.world.level.GameRules;

public final class TOKGameRules {

    /**
     * When enabled (the default), the Guild generates naturally as a world structure within
     * 2000 blocks of 0,0 and players name their conquest when signing the contract. When
     * disabled, the legacy behaviour applies: the start conquest screen prompts the player
     * to build the Guild wherever they stand.
     */
    public static final GameRules.Key<GameRules.BooleanValue> AUTO_GENERATE_GUILD = GameRulesInvoker.callRegister(
            "autoGenerateGuild", GameRules.Category.MISC, GameRulesBooleanValueInvoker.callCreate(true));

    private TOKGameRules() {}

    /**
     * Forces class initialisation so the rules register during mod init, before any world exists.
     */
    public static void init() {}
}
