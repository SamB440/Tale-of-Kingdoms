package com.convallyria.taleofkingdoms.common.quest.objective;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.quest.Quest;
import com.convallyria.taleofkingdoms.managers.registry.QuesteRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

public final class QuestObjectiveRegistry extends QuesteRegistry<QuestObjective> {

    @Nullable
    public QuestObjective getNewObjective(String name, TaleOfKingdomsAPI plugin, Quest quest) {
        return getNewObjective(getRegisteredClasses().get(name), plugin, quest);
    }

    @Nullable
    public QuestObjective getNewObjective(Class<? extends QuestObjective> clazz, TaleOfKingdomsAPI plugin, Quest quest) {
        try {
            Constructor<?> constructor = clazz.getConstructor(TaleOfKingdomsAPI.class, Quest.class);
            return (QuestObjective) constructor.newInstance(plugin, quest);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @deprecated Constructor is invalid.
     * {@link #getNewObjective(String, Queste, Quest)} {@link #getNewObjective(Class, Queste, Quest)}
     */
    @Deprecated
    @Override
    public @Nullable QuestObjective getNew(Class<? extends QuestObjective> clazz, TaleOfKingdomsAPI plugin) {
        throw new IllegalStateException("Use getNewObjective instead");
    }

    @Override
    public String getRegistryName() {
        return "objectives";
    }
}
