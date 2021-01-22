package com.convallyria.taleofkingdoms.common.quest.reward;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Arrays;
import java.util.List;

/**
 * Reward to send messages to the player. More complex messages can use the tellraw command in {@link ConsoleCommandReward}.
 */
public final class MessageReward extends QuestReward {

    private final List<String> messages;

    public MessageReward(TaleOfKingdoms plugin) {
        super(plugin);
        this.messages = Arrays.asList("Message one!", "Message two!");
    }

    public MessageReward(TaleOfKingdoms plugin, List<String> messages) {
        super(plugin);
        this.messages = messages;
    }

    @Override
    public void award(PlayerEntity player) {
        messages.forEach(message -> player.sendMessage(new LiteralText(message), false));
    }

    @Override
    public String getName() {
        return "Message";
    }
}
