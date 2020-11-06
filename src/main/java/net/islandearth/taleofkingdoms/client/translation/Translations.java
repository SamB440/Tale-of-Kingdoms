package net.islandearth.taleofkingdoms.client.translation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

public enum Translations {
    FARMER_TAKE_BREAD("entity_type.taleofkingdoms.farmer.take_bread"),
    FARMER_GOT_BREAD("entity_type.taleofkingdoms.farmer.got_bread"),
    GUILDMASTER_CONTRACT_SIGN("entity_type.taleofkingdoms.guildmaster.contract.sign"),
    GUILDMASTER_CONTRACT_SIGN_UP("entity_type.taleofkingdoms.guildmaster.contract.sign_up"),
    GUILDMASTER_CONTRACT_CANCEL("entity_type.taleofkingdoms.guildmaster.contract.cancel"),
    GUILDMASTER_WELCOME("entity_type.taleofkingdoms.guildmaster.welcome"),
    GUILDMASTER_GOODHUNTING("entity_type.taleofkingdoms.guildmaster.good_hunting"),
    INNKEEPER_REST("entity_type.taleofkingdoms.innkeeper.rest"),
    INNKEEPER_LEAVE("entity_type.taleofkingdoms.innkeeper.leave"),
    NEED_CONTRACT("generic.taleofkingdoms.need_contract"),
    SERVE("generic.taleofkingdoms.serve");
    
    private final String key;
    
    Translations(String key) {
        this.key = key;
    }
    
    public void send(PlayerEntity playerEntity) {
        playerEntity.sendMessage(getTranslation(), false);
    }

    public void send(PlayerEntity playerEntity, String... values) {
        send(playerEntity, false, values);
    }

    public void send(PlayerEntity playerEntity, boolean actionbar, String... values) {
        playerEntity.sendMessage(new LiteralText(replaceVariables(getTranslation().getString(), values)), actionbar);
    }

    @NotNull
    private String replaceVariables(String message, String... values) {
        String modifiedMessage = message;
        for (int i = 0; i < 10; i++) {
            if (values.length > i) modifiedMessage = modifiedMessage.replaceAll("%" + i, values[i]);
            else break;
        }

        return modifiedMessage;
    }

    public TranslatableText getTranslation() {
        return new TranslatableText(key);
    }
    
    public String getFormatted() {
        return getTranslation().getString();
    }
    
    public String getKey() {
        return key;
    }
}
