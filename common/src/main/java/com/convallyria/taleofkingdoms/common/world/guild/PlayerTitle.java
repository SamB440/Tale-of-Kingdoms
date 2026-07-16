package com.convallyria.taleofkingdoms.common.world.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;

/**
 * The title NPCs use when addressing a player, e.g. "Thank you my liege!".
 */
public enum PlayerTitle {
    KING,
    QUEEN,
    LIEGE;

    public static final PlayerTitle DEFAULT = LIEGE;

    public static final Codec<PlayerTitle> CODEC = Codec.STRING.xmap(
            name -> PlayerTitle.valueOf(name.toUpperCase(Locale.ROOT)),
            PlayerTitle::getName);

    private final String key;
    private final String addressKey;

    PlayerTitle() {
        this.key = "generic.taleofkingdoms.title." + getName();
        this.addressKey = key + ".address";
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Component getTranslation() {
        return Component.translatable(key);
    }

    /**
     * The polite form of address NPCs use in casual speech, e.g. "my lord" rather than "King".
     */
    public Component getAddress() {
        return Component.translatable(addressKey);
    }

    /**
     * Gets the title the player has chosen, or {@link #DEFAULT} if they have not chosen one
     * or are not part of a conquest instance.
     * @param player the player being addressed
     * @return the title to address the player with
     */
    public static PlayerTitle of(Player player) {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return DEFAULT;
        return api.getConquestInstanceStorage().mostRecentInstance()
                .map(instance -> instance.getPlayer(player))
                .map(GuildPlayer::getTitle)
                .orElse(DEFAULT);
    }
}
