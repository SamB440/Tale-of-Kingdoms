package com.convallyria.taleofkingdoms.common.packet;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.util.Identifier;

public class Packets {

    public static final Identifier INSTANCE_SYNC = register("instance_sync");
    public static final Identifier OPEN_CLIENT_SCREEN = register("open_client_screen");
    public static final Identifier SIGN_CONTRACT = register("sign_contract");
    public static final Identifier FIX_GUILD = register("fix_guild");
    public static final Identifier TOGGLE_SELL_GUI = register("toggle_sell_gui");
    public static final Identifier BUY_ITEM = register("buy_item");
    public static final Identifier BANKER_INTERACT = register("banker_interact");
    public static final Identifier HIRE_HUNTER = register("hire_hunter");
    public static final Identifier INNKEEPER_HIRE_ROOM = register("innkeeper_hire_room");
    public static final Identifier BUILD_KINGDOM = register("build_kingdom");
    public static final Identifier FOREMAN_BUY_WORKER = register("foreman_buy_worker");
    public static final Identifier FOREMAN_COLLECT = register("foreman_collect");
    public static final Identifier CITYBUILDER_ACTION = register("citybuilder_action");
    
    private static Identifier register(String id) {
        return new Identifier(TaleOfKingdoms.MODID, id);
    }
}
