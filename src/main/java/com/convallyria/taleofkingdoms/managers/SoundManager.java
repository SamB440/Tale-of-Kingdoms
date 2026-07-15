package com.convallyria.taleofkingdoms.managers;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

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
        ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, sound.getPath());
        TaleOfKingdoms.LOGGER.info("Loading sound: {}", sound.getPath());
        events.put(sound, SoundEvent.createVariableRangeEvent(identifier));
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
            ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, name.getPath());
            Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, event);
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
