package com.convallyria.taleofkingdoms.managers;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class SoundManager implements IManager {

    private final Map<TOKSound, SoundEvent> events = new HashMap<>();

    public SoundManager(TaleOfKingdoms tok) {
        TaleOfKingdoms.LOGGER.info("Loading sounds...");
        for (TOKSound value : TOKSound.values()) {
            addSound(value);
        }
        register();
    }

    private void addSound(TOKSound sound) {
        Identifier identifier = new Identifier(TaleOfKingdoms.MODID, sound.getPath());
        TaleOfKingdoms.LOGGER.info("Loading sound: " + sound.getPath());
        events.put(sound, new SoundEvent(identifier));
    }

    public SoundEvent getSound(TOKSound sound) {
        return events.get(sound);
    }

    @Override
    public String getName() {
        return "Sound Manager";
    }

    private void register() {
        events.forEach((name, event) -> {
            Identifier identifier = new Identifier(TaleOfKingdoms.MODID, name.getPath());
            Registry.register(Registry.SOUND_EVENT, identifier, event);
        });
    }

    public enum TOKSound {
        TOKTHEME("toktheme");

        private final String path;

        TOKSound(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
