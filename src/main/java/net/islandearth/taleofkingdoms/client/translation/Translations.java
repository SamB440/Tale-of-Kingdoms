package net.islandearth.taleofkingdoms.client.translation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public enum Translations {
    FARMER_TAKE_BREAD("taleofkingdoms.entity.farmer.take_bread"),
    FARMER_GOT_BREAD("taleofkingdoms.entity.farmer.got_bread"),
    GUILDMASTER_CONTRACT_SIGN("taleofkingdoms.entity.guildmaster.contract.sign"),
    GUILDMASTER_CONTRACT_SIGN_UP("taleofkingdoms.entity.guildmaster.contract.sign_up"),
    GUILDMASTER_CONTRACT_CANCEL("taleofkingdoms.entity.guildmaster.contract.cancel"),
    GUILDMASTER_WELCOME("taleofkingdoms.entity.guildmaster.welcome"),
    GUILDMASTER_GOODHUNTING("taleofkingdoms.entity.guildmaster.good_hunting");
    
    private final String key;
    
    Translations(String key) {
        this.key = key;
    }
    
    public void send(PlayerEntity playerEntity) {
        playerEntity.sendMessage(getTranslation());
    }
    
    public TranslationTextComponent getTranslation() {
        return new TranslationTextComponent(key);
    }
    
    public String getFormatted() {
        return getTranslation().getFormattedText();
    }
    
    public String getRaw() {
        return getTranslation().getUnformattedComponentText();
    }
    
    public String getKey() {
        return key;
    }
}
