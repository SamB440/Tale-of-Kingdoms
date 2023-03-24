package com.convallyria.taleofkingdoms.common.packet;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.util.Identifier;

public class Packets {

    public static final Identifier INSTANCE_SYNC = new Identifier(TaleOfKingdoms.MODID, "instance_sync");
    public static final Identifier SIGN_CONTRACT = new Identifier(TaleOfKingdoms.MODID, "sign_contract");
    public static final Identifier FIX_GUILD = new Identifier(TaleOfKingdoms.MODID, "fix_guild");
    public static final Identifier TOGGLE_SELL_GUI = new Identifier(TaleOfKingdoms.MODID, "toggle_sell_gui");
    public static final Identifier BUY_ITEM = new Identifier(TaleOfKingdoms.MODID, "buy_item");
    public static final Identifier BANKER_INTERACT = new Identifier(TaleOfKingdoms.MODID, "banker_interact");
    public static final Identifier HIRE_HUNTER = new Identifier(TaleOfKingdoms.MODID, "hire_hunter");
    public static final Identifier INNKEEPER_HIRE_ROOM = new Identifier(TaleOfKingdoms.MODID, "innkeeper_hire_room");
    public static final Identifier BUILD_KINGDOM = new Identifier(TaleOfKingdoms.MODID, "build_kingdom");
    public static final Identifier FOREMAN_BUY_WORKER = new Identifier(TaleOfKingdoms.MODID, "foreman_buy_worker");
    public static final Identifier FOREMAN_COLLECT = new Identifier(TaleOfKingdoms.MODID, "foreman_collect");
    public static final Identifier CITYBUILDER_ACTION = new Identifier(TaleOfKingdoms.MODID, "citybuilder_action");
}
