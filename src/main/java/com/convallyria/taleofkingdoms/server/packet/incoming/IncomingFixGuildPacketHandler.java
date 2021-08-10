package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.schematic.SchematicOptions;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IncomingFixGuildPacketHandler extends ServerPacketHandler {

    private long lastRebuild;

    public IncomingFixGuildPacketHandler() {
        super(TaleOfKingdoms.FIX_GUILD_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(ResourceLocation identifier, PacketContext context, FriendlyByteBuf attachedData) {
        ServerPlayer player = (ServerPlayer) context.player();
        String playerContext = " @ <" + player.getName().getContents() + ":" + player.getIpAddress() + ">";
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                api.getConquestInstanceStorage().mostRecentInstance().ifPresent(inst -> {
                    ServerConquestInstance instance = (ServerConquestInstance) inst;
                    Inventory playerInventory = player.getInventory();
                    ItemStack stack = InventoryUtils.getStack(playerInventory, ItemTags.LOGS.getValues(), 64);
                    if (stack == null) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Inventory requirement not met. Data mismatch?");
                        return;
                    }

                    if (instance.getCoins(player.getUUID()) < 3000) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Coin requirement not met. Data mismatch?");
                        return;
                    }

                    TaleOfKingdoms.LOGGER.info("Guild rebuild requested " + playerContext + ".");
                    long now = System.currentTimeMillis();
                    int seconds = (int) ((now - this.lastRebuild) / 1000);
                    if (seconds <= 60) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Rebuilt too recently.");
                        int secondsLeft = 60 - seconds;
                        player.displayClientMessage(new TextComponent("The guild was only rebuilt " + seconds + " seconds ago! Please wait " + secondsLeft + " more seconds."), false);
                        return;
                    }

                    playerInventory.setItem(InventoryUtils.getSlotWithStack(playerInventory, stack), new ItemStack(Items.AIR));
                    instance.setCoins(player.getUUID(), instance.getCoins(player.getUUID()) - 3000);
                    instance.rebuild(player, api, SchematicOptions.IGNORE_DEFENDERS);
                    instance.sync(player, null);
                    this.lastRebuild = System.currentTimeMillis();
                });
            });
        });
    }



    @Override
    public void handleOutgoingPacket(ResourceLocation identifier, @NotNull Player player,
                                     @Nullable Connection connection, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
