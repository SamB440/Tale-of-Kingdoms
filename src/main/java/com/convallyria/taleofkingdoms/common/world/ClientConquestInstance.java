package com.convallyria.taleofkingdoms.common.world;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.quest.Quest;
import com.convallyria.taleofkingdoms.common.quest.objective.QuestObjective;
import com.convallyria.taleofkingdoms.common.scheduler.Scheduler;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.BossBarManager;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClientConquestInstance extends ConquestInstance {
    
    private final transient Map<String, Integer> time = new HashMap<>();
    
    private int coins;
    private int bankerCoins;
    private long farmerLastBread;
    private boolean hasContract;
    private int worthiness;
    private List<UUID> hunterUUIDs;
    private final List<Quest> activeQuests;
    private final List<Quest> completedQuests;

    public ClientConquestInstance(String world, String name, BlockPos start, BlockPos end, BlockPos origin) {
        super(world, name, start, end, origin);
        this.hunterUUIDs = new ArrayList<>();
        this.activeQuests = new ArrayList<>();
        this.completedQuests = new ArrayList<>();
    }

    public int getCoins() {
        return coins;
    }

    public int getBankerCoins() { return bankerCoins; }

    public void setBankerCoins(int bankerCoins) { this.bankerCoins = bankerCoins; }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int coins) {
        this.coins = this.coins + coins;
    }

    public long getFarmerLastBread() {
        return farmerLastBread;
    }

    public void setFarmerLastBread(long day) {
        this.farmerLastBread = day;
    }

    public boolean hasContract() {
        return hasContract;
    }

    public void setHasContract(boolean hasContract) {
        this.hasContract = hasContract;
    }

    public int getWorthiness() {
        return worthiness;
    }

    public void setWorthiness(int worthiness) {
        this.worthiness = worthiness;
    }

    public void addWorthiness(int worthiness) {
        this.worthiness = this.worthiness + worthiness;
    }

    public void addHunter(Entity entity) {
        this.hunterUUIDs.add(entity.getUuid());
    }

    public ImmutableList<UUID> getHunterUUIDs() {
        if (hunterUUIDs == null) this.hunterUUIDs = new ArrayList<>();
        return ImmutableList.copyOf(hunterUUIDs);
    }

    public void removeHunter(Entity entity) {
        this.hunterUUIDs.remove(entity.getUuid());
    }
    
    @Override
    public List<Quest> getActiveQuests(PlayerEntity player) {
        return activeQuests;
    }
    
    @Override
    public List<Quest> getCompletedQuests(PlayerEntity player) {
        return completedQuests;
    }
    
    @Override
    public void addActiveQuest(Quest quest, PlayerEntity player) {
        activeQuests.add(quest);
        QuestObjective currentObjective = quest.getCurrentObjective(player);
        if (currentObjective != null) {
            TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
            IntegratedServer server = MinecraftClient.getInstance().getServer();
            BossBarManager manager = server.getBossBarManager();
            CommandBossBar activeBar = manager.get(new Identifier(TaleOfKingdoms.MODID, player.getUuid() + quest.getName()));
            if (activeBar == null) {
                activeBar = manager.add(new Identifier(TaleOfKingdoms.MODID, player.getUuid() + quest.getName()),
                        Translations.OBJECTIVE_PROGRESS.get(currentObjective.getDisplayName(),
                                currentObjective.getIncrement(player), currentObjective.getCompletionAmount()));
                activeBar.setColor(BossBar.Color.WHITE);
                activeBar.setOverlay(BossBar.Style.NOTCHED_10);
            }
            activeBar.setPercent(0);
            activeBar.addPlayer(player.getUuid());
            update(quest, player);
            
            if (quest.getTime() > 0) {
                time.put(quest.getName(), quest.getTime());
                api.getScheduler().repeatN(minecraftServer -> {
                    int timeLeft = time.get(quest.getName());
                    time.put(quest.getName(), timeLeft - 1);
                    CommandBossBar bossBar = manager.get(new Identifier(TaleOfKingdoms.MODID, player.getUuid() + quest.getName()));
                    bossBar.setName(new LiteralText(Translations.OBJECTIVE_PROGRESS.get(player, currentObjective.getDisplayName(),
                            currentObjective.getIncrement(player), currentObjective.getCompletionAmount()) + Formatting.GRAY.toString() + " (" + timeLeft + "s)"));
                }, quest.getTime(), 0, 20, completed -> {
                    if (!quest.isCompleted(player)) {
                        CommandBossBar bossBar = manager.get(new Identifier(TaleOfKingdoms.MODID, player.getUuid() + quest.getName()));
                        activeQuests.remove(quest);
                        time.remove(quest.getName());
                        bossBar.clearPlayers();
                        player.playSound(SoundEvents.ENTITY_ENDER_DRAGON_DEATH, 1f, 1f);
                        //player.sendTitle(Translations.QUEST_FAILED_TITLE.get(player), quest.getDisplayName(), 40, 60, 40);
                    }
                });
            }
        }
    }
    
    @Override
    public void removeActiveQuest(Quest quest, PlayerEntity player) {
        activeQuests.remove(quest);
        update(quest, player);
    }
    
    @Override
    public void removeCompletedQuest(Quest quest, PlayerEntity player) {
        completedQuests.remove(quest);
    }
    
    @Override
    public void update(Quest quest, PlayerEntity player) {
        TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
        IntegratedServer server = MinecraftClient.getInstance().getServer();
        BossBarManager manager = server.getBossBarManager();
        CommandBossBar commandBossBar = manager.add(new Identifier(TaleOfKingdoms.MODID, player.getUuid() + quest.getName()), new LiteralText("bossbar"));
        if (commandBossBar == null) return;
        QuestObjective currentObjective = quest.getCurrentObjective(player);
        if (currentObjective != null) {
            int increment = currentObjective.getIncrement(player);
            float percent = (increment * 100.0f) / currentObjective.getCompletionAmount();
            if (quest.getTime() == 0)
                commandBossBar.setName(Translations.OBJECTIVE_PROGRESS.get(player, currentObjective.getDisplayName(), increment, currentObjective.getCompletionAmount()));
            commandBossBar.setPercent(percent / 100);
        } else {
            commandBossBar.clearPlayers();
        }
    }
    
    @Override
    public void addCompletedQuest(Quest quest, PlayerEntity player) {
        completedQuests.add(quest);
    }
}
