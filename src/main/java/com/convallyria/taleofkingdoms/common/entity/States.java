package com.convallyria.taleofkingdoms.common.entity;

public interface States {

    State getState();

    enum State {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        CELEBRATING,
        NEUTRAL
    }
}