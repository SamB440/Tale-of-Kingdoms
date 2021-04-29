package com.convallyria.taleofkingdoms.common.sounds;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Sounds {

    private static final Identifier RAT_GRUNT_IDENTIFIER = new Identifier(TaleOfKingdoms.MODID, "ratgrunt");
    public static final SoundEvent RAT_GRUNT = Registry.register(Registry.SOUND_EVENT, RAT_GRUNT_IDENTIFIER, new SoundEvent(RAT_GRUNT_IDENTIFIER));

    private static final Identifier RAT_HURT_IDENTIFIER = new Identifier(TaleOfKingdoms.MODID, "rathurt");
    public static final SoundEvent RAT_HURT = Registry.register(Registry.SOUND_EVENT, RAT_HURT_IDENTIFIER, new SoundEvent(RAT_HURT_IDENTIFIER));

    private static final Identifier RAT_DEATH_IDENTIFIER = new Identifier(TaleOfKingdoms.MODID, "ratdeath");
    public static final SoundEvent RAT_DEATH = Registry.register(Registry.SOUND_EVENT, RAT_DEATH_IDENTIFIER, new SoundEvent(RAT_DEATH_IDENTIFIER));

    private static final Identifier BOAR_AMBIENT_IDENTIFIER = new Identifier(TaleOfKingdoms.MODID, "boarambient");
    public static final SoundEvent BOAR_AMBIENT = Registry.register(Registry.SOUND_EVENT, BOAR_AMBIENT_IDENTIFIER, new SoundEvent(BOAR_AMBIENT_IDENTIFIER));

    private static final Identifier BOAR_HURT_IDENTIFIER = new Identifier(TaleOfKingdoms.MODID, "boarhurt");
    public static final SoundEvent BOAR_HURT = Registry.register(Registry.SOUND_EVENT, BOAR_HURT_IDENTIFIER, new SoundEvent(BOAR_HURT_IDENTIFIER));
}
