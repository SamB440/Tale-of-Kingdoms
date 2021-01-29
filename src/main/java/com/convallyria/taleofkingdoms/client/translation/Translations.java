package com.convallyria.taleofkingdoms.client.translation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

public enum Translations {
    NEXT_PAGE("quest.next_page"),
    NEXT_PAGE_LORE("quest.next_page_lore"),
    PREVIOUS_PAGE("quest.previous_page"),
    PREVIOUS_PAGE_LORE("quest.previous_page_lore"),
    EXIT("quest.exit"),
    EXIT_LORE("quest.exit_lore"),
    OBJECTIVE_COMPLETE("quest.objective_complete"),
    QUEST_COMPLETED("quest.quest_completed"),
    QUEST_COMPLETED_TITLE("quest.quest_completed_title"),
    QUEST_FAILED_TITLE("quest.quest_failed_title"),
    QUEST_STARTED("quest.quest_started"),
    OBJECTIVE_PROGRESS("quest.objective_progress"),
    FARMER_TAKE_BREAD("entity_type.taleofkingdoms.farmer.take_bread"),
    FARMER_GOT_BREAD("entity_type.taleofkingdoms.farmer.got_bread"),
    GUILDMASTER_CONTRACT_SIGN("entity_type.taleofkingdoms.guildmaster.contract.sign"),
    GUILDMASTER_CONTRACT_SIGN_UP("entity_type.taleofkingdoms.guildmaster.contract.sign_up"),
    GUILDMASTER_CONTRACT_CANCEL("entity_type.taleofkingdoms.guildmaster.contract.cancel"),
    GUILDMASTER_WELCOME("entity_type.taleofkingdoms.guildmaster.welcome"),
    GUILDMASTER_NOHUNTER("entity_type.taleofkingdoms.guildmaster.no_hunter"),
    GUILDMASTER_GOODHUNTING("entity_type.taleofkingdoms.guildmaster.good_hunting"),
    CITYBUILDER_MESSAGE("entity_type.taleofkingdoms.citybuilder.message"),
    INNKEEPER_REST("entity_type.taleofkingdoms.innkeeper.rest"),
    INNKEEPER_LEAVE("entity_type.taleofkingdoms.innkeeper.leave"),
    NEED_CONTRACT("generic.taleofkingdoms.need_contract"),
    SERVE("generic.taleofkingdoms.serve"),
    LONE_HELP("entity_type.taleofkingdoms.lone.help"),
    SHOP_CLOSE("entity_type.taleofkingdoms.shop.close"),
    BANK_OPEN("entity_type.taleofkingdoms.bank.open"),
    BANK_INPUT("entity_type.taleofkingdoms.bank.input"),
    BANK_ZERO("entity_type.taleofkingdoms.bank.zero"),
    BANK_NO_SPEND("entity_type.taleofkingdoms.bank.no_spend"),
    GUILDMEMBER_START("entity_type.taleofkingdoms.guildmember.first"),
    HUNTER_BOW("entity_type.taleofkingdoms.hunter.bow"),
    HUNTER_SWORD("entity_type.taleofkingdoms.hunter.sword"),
    HUNTER_THANK("entity_type.taleofkingdoms.hunter.thank"),
    GOLD_COINS("generic.taleofkingdoms.gold_coins"),
    GUILDMASTER_GUILD_ORDER("entity_type.taleofkingdoms.guildmaster.guild_order"),
    GUILDMASTER_PATH("entity_type.taleofkingdoms.guildmaster.path"),
    START_CONQUEST("generic.taleofkingdoms.start_conquest"),
    BUILDING_CASTLE("generic.taleofkingdoms.building_castle"),
    SUMMONING_CITIZENS("generic.taleofkingdoms.summon_citizens"),
    NEW_CITIZEN("generic.taleofkingdoms.new_citizen"),
    DARKNESS("generic.taleofkingdoms.darkness"),
    HERO("generic.taleofkingdoms.hero");
    
    private final String key;
    
    Translations(String key) {
        this.key = key;
    }
    
    public void send(PlayerEntity playerEntity) {
        playerEntity.sendMessage(getTranslation(), false);
    }

    public void send(PlayerEntity playerEntity, Object... values) {
        send(playerEntity, false, values);
    }

    public void send(PlayerEntity playerEntity, boolean actionbar, Object... values) {
        playerEntity.sendMessage(new LiteralText(replaceVariables(getTranslation().asString(), values)), actionbar);
    }

    @NotNull
    private String replaceVariables(String message, Object... values) {
        String modifiedMessage = message;
        for (int i = 0; i < 10; i++) {
            if (values.length > i) modifiedMessage = modifiedMessage.replaceAll("%" + i, String.valueOf(values[i]));
            else break;
        }

        return modifiedMessage;
    }

    public TranslatableText getTranslation() {
        return new TranslatableText(key);
    }
    
    public LiteralText get(Object... values) {
        return new LiteralText(replaceVariables(getTranslation().asString(), values));
    }
    
    public String getFormatted() {
        return getTranslation().getString();
    }
    
    public String getKey() {
        return key;
    }
}
