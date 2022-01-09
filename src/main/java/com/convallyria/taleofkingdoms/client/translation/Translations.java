package com.convallyria.taleofkingdoms.client.translation;

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
    GUILDMASTER_NOHUNTER("entity_type.taleofkingdoms.guildmaster.no_hunter"),
    GUILDMASTER_GOODHUNTING("entity_type.taleofkingdoms.guildmaster.good_hunting"),
    GUILDMASTER_HELP("entity_type.taleofkingdoms.guildmaster.help"),
    GUILDMASTER_STAY_CLOSE("entity_type.taleofkingdoms.guildmaster.stay_close"),
    GUILDMASTER_KILL_REFICULES("entity_type.taleofkingdoms.guildmaster.kill_reficules"),
    GUILDMASTER_REBUILD("entity_type.taleofkingdoms.guildmaster.rebuild"),
    GUILDMASTER_THANK_YOU("entity_type.taleofkingdoms.guildmaster.thank_you"),
    GUILDMASTER_GUILD_ORDER("entity_type.taleofkingdoms.guildmaster.guild_order"),
    GUILDMASTER_PATH("entity_type.taleofkingdoms.guildmaster.path"),

    CITYBUILDER_MESSAGE("entity_type.taleofkingdoms.citybuilder.message"),
    CITYBUILDER_BUILD("entity_type.taleofkingdoms.citybuilder.build"),
    CITYBUILDER_DISTANCE("entity_type.taleofkingdoms.citybuilder.distance"),

    INNKEEPER_REST("entity_type.taleofkingdoms.innkeeper.rest"),
    INNKEEPER_LEAVE("entity_type.taleofkingdoms.innkeeper.leave"),

    NEED_CONTRACT("generic.taleofkingdoms.need_contract"),
    SERVE("generic.taleofkingdoms.serve"),
    GOLD_COINS("generic.taleofkingdoms.gold_coins"),
    START_CONQUEST("generic.taleofkingdoms.start_conquest"),
    BUILDING_CASTLE("generic.taleofkingdoms.building_castle"),
    SUMMONING_CITIZENS("generic.taleofkingdoms.summon_citizens"),
    NEW_CITIZEN("generic.taleofkingdoms.new_citizen"),
    DARKNESS("generic.taleofkingdoms.darkness"),
    HERO("generic.taleofkingdoms.hero"),

    LONE_HELP("entity_type.taleofkingdoms.lone.help"),
    LONE_THANK("entity_type.taleofkingdoms.lone.thank"),

    SHOP_CLOSE("entity_type.taleofkingdoms.shop.close"),

    BANK_OPEN("entity_type.taleofkingdoms.bank.open"),
    BANK_INPUT("entity_type.taleofkingdoms.bank.input"),
    BANK_ZERO("entity_type.taleofkingdoms.bank.zero"),
    BANK_NO_SPEND("entity_type.taleofkingdoms.bank.no_spend"),

    GUILDMEMBER_START("entity_type.taleofkingdoms.guildmember.first"),
    GUILDMEMBER_FIGHTER("entity_type.taleofkingdoms.guildmember.fighter"),
    GUILDMEMBER_START_FIGHT("entity_type.taleofkingdoms.guildmember.start_fight"),
    GUILDMEMBER_BEGIN("entity_type.taleofkingdoms.guildmember.begin"),
    GUILDMEMBER_GOOD_FIGHTER("entity_type.taleofkingdoms.guildmember.good_fighter"),

    HUNTER_BOW("entity_type.taleofkingdoms.hunter.bow"),
    HUNTER_SWORD("entity_type.taleofkingdoms.hunter.sword"),
    HUNTER_THANK("entity_type.taleofkingdoms.hunter.thank"),

    LOST_VILLAGER_THANK("entity_type.taleofkingdoms.lostvillager.thank"),
    LOST_VILLAGER_GUILD_THANK("entity_type.taleofkingdoms.lostvillager.guild_thank");
    
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
        playerEntity.sendMessage(new LiteralText(replaceVariables(getTranslation().getString(), values)), actionbar);
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
    
    public String getFormatted() {
        return getTranslation().getString();
    }
    
    public String getKey() {
        return key;
    }
}
