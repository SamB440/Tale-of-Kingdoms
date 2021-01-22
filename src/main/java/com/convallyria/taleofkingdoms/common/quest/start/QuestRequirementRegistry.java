package com.convallyria.taleofkingdoms.common.quest.start;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.managers.registry.QuesteRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

public final class QuestRequirementRegistry extends QuesteRegistry<QuestRequirement> {

    @Override
    public @Nullable QuestRequirement getNew(Class<? extends QuestRequirement> clazz, TaleOfKingdomsAPI plugin) {
        try {
            Constructor<?> constructor = clazz.getConstructor(TaleOfKingdomsAPI.class);
            return (QuestRequirement) constructor.newInstance(plugin);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getRegistryName() {
        return "requirements";
    }
}
