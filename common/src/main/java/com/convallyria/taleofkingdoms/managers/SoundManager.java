package com.convallyria.taleofkingdoms.managers;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundManager implements IManager {

    /**
     * Deferred sound event registrations. Direct {@code Registry.register} calls are not possible
     * here because this manager is constructed during mod init, after NeoForge has frozen
     * {@code BuiltInRegistries}; Architectury commits these when register() is called from
     * {@link TaleOfKingdoms#init()} alongside the other registries.
     */
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(TaleOfKingdoms.MODID, Registries.SOUND_EVENT);

    private static final Map<TOKSound, RegistrySupplier<SoundEvent>> EVENTS = new EnumMap<>(TOKSound.class);

    static {
        for (TOKSound sound : TOKSound.values()) {
            ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, sound.getPath());
            EVENTS.put(sound, SOUND_EVENTS.register(sound.getPath(), () -> SoundEvent.createVariableRangeEvent(identifier)));
        }
    }

    public SoundManager(TaleOfKingdoms tok) {
        TaleOfKingdoms.LOGGER.info("Loading sounds...");
    }

    public SoundEvent getSound(TOKSound sound) {
        return EVENTS.get(sound).get();
    }

    @Override
    public String getName() {
        return "Sound Manager";
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
