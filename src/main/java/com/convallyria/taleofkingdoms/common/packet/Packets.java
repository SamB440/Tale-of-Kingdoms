package com.convallyria.taleofkingdoms.common.packet;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.util.Identifier;

public class Packets {

    public static final Identifier INSTANCE_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "instance");
    public static final Identifier SIGN_CONTRACT_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "sign_contract");
    public static final Identifier FIX_GUILD_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "fix_guild");
    public static final Identifier TOGGLE_SELL_GUI_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "open_sell_gui");
    public static final Identifier BUY_ITEM_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "buy_item");
    public static final Identifier BANKER_INTERACT_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "banker_interact");
    public static final Identifier HUNTER_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "hunter");
    public static final Identifier INNKEEPER_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "innkeeper");
    public static final Identifier BUILD_KINGDOM_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "build_kingdom");
}
