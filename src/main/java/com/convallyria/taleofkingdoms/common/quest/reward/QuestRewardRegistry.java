package com.convallyria.taleofkingdoms.common.quest.reward;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.managers.registry.QuesteRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

public final class QuestRewardRegistry extends QuesteRegistry<QuestReward> {

    @Nullable
    public QuestReward getNew(Class<? extends QuestReward> clazz, TaleOfKingdomsAPI plugin) {
        try {
            Constructor<?> constructor = clazz.getConstructor(TaleOfKingdomsAPI.class);
            return (QuestReward) constructor.newInstance(plugin);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getRegistryName() {
        return "rewards";
    }
}
