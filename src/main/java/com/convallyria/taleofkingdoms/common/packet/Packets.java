package com.convallyria.taleofkingdoms.common.packet;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.both.SignContractPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.BankerInteractPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.BuildKingdomPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.BuyItemPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.CityBuilderActionPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.FixGuildPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.ForemanBuyWorkerPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.ForemanCollectPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.HireHunterPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.InnkeeperActionPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.ToggleSellGuiPacket;
import com.convallyria.taleofkingdoms.common.packet.c2s.UpgradeKingdomPacket;
import com.convallyria.taleofkingdoms.common.packet.s2c.InstanceSyncPacket;
import com.convallyria.taleofkingdoms.common.packet.s2c.OpenScreenPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class Packets {

    public static final CustomPacketPayload.Type<InstanceSyncPacket> INSTANCE_SYNC = register("instance_sync");
    public static final CustomPacketPayload.Type<OpenScreenPacket> OPEN_CLIENT_SCREEN = register("open_client_screen");
    public static final CustomPacketPayload.Type<SignContractPacket> SIGN_CONTRACT = register("sign_contract");
    public static final CustomPacketPayload.Type<FixGuildPacket> FIX_GUILD = register("fix_guild");
    public static final CustomPacketPayload.Type<ToggleSellGuiPacket> TOGGLE_SELL_GUI = register("toggle_sell_gui");
    public static final CustomPacketPayload.Type<BuyItemPacket> BUY_ITEM = register("buy_item");
    public static final CustomPacketPayload.Type<BankerInteractPacket> BANKER_INTERACT = register("banker_interact");
    public static final CustomPacketPayload.Type<HireHunterPacket> HIRE_HUNTER = register("hire_hunter");
    public static final CustomPacketPayload.Type<InnkeeperActionPacket> INNKEEPER_HIRE_ROOM = register("innkeeper_hire_room");
    public static final CustomPacketPayload.Type<BuildKingdomPacket> BUILD_KINGDOM = register("build_kingdom");
    public static final CustomPacketPayload.Type<UpgradeKingdomPacket> UPGRADE_KINGDOM = register("upgrade_kingdom");
    public static final CustomPacketPayload.Type<ForemanBuyWorkerPacket> FOREMAN_BUY_WORKER = register("foreman_buy_worker");
    public static final CustomPacketPayload.Type<ForemanCollectPacket> FOREMAN_COLLECT = register("foreman_collect");
    public static final CustomPacketPayload.Type<CityBuilderActionPacket> CITYBUILDER_ACTION = register("citybuilder_action");
    
    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> register(String id) {
        return new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, id));
    }
}
