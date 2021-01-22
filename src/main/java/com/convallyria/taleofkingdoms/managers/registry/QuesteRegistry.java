package com.convallyria.taleofkingdoms.managers.registry;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class QuesteRegistry<T> {

    private final Map<String, Class<? extends T>> registeredClasses;

    protected QuesteRegistry() {
        this.registeredClasses = new ConcurrentHashMap<>();
    }

    @NotNull
    protected Map<String, Class<? extends T>> getRegisteredClasses() {
        return registeredClasses;
    }

    @NotNull
    public ImmutableMap<String, Class<? extends T>> get() {
        return ImmutableMap.copyOf(registeredClasses);
    }

    /**
     * Attempts to register a class.
     * @param clazz class to register
     * @throws IllegalArgumentException if class is already registered
     */
    public void register(Class<? extends T> clazz) {
        if (registeredClasses.containsKey(clazz.getSimpleName()))
            throw new IllegalArgumentException(clazz.getSimpleName() + " is already registered!");
        registeredClasses.put(clazz.getSimpleName(), clazz);
    }

    @Nullable
    public T getNew(String name, TaleOfKingdomsAPI plugin) {
        return getNew(registeredClasses.get(name), plugin);
    }

    @Nullable
    public abstract T getNew(Class<? extends T> clazz, TaleOfKingdomsAPI plugin);

    public abstract String getRegistryName();

}
