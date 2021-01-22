package com.convallyria.taleofkingdoms.managers.data;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.quest.Quest;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuesteCache {

    private final TaleOfKingdoms plugin;
    private final Map<String, Quest> quests;

    public QuesteCache(TaleOfKingdoms plugin) {
        this.plugin = plugin;
        this.quests = new ConcurrentHashMap<>();
    }

    public Map<String, Quest> getQuests() {
        return quests;
    }

    /*public void reload() {
        quests.clear();
        File folder = new File(plugin.getDataFolder() + "/quests/");
        if (!folder.exists()) folder.mkdirs();
        for (File file : folder.listFiles()) {
            try {
                Reader reader = new FileReader(file);
                Quest quest = plugin.getGson().fromJson(reader, Quest.class);
                quest.getObjectives().forEach(QuestObjective::registerListeners);
                System.out.println("Loaded quest " + quest.getName() + ".");
                addQuest(quest);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }*/

    @Nullable
    public Quest getQuest(String name) {
        return quests.get(name);
    }

    public void addQuest(Quest quest) {
        quests.put(quest.getName(), quest);
    }

    public void removeQuest(Quest quest) {
        quests.remove(quest.getName());
    }
}
