package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.schematic.SchematicOptions;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IncomingFixGuildPacketHandler extends ServerPacketHandler {

    private long lastRebuild;

    public IncomingFixGuildPacketHandler() {
        super(TaleOfKingdoms.FIX_GUILD_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.player();
        String playerContext = identifier.toString() + " @ <" + player.getName().getString() + ":" + player.getIp() + ">";
        context.taskQueue().execute(() -> {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                PlayerInventory playerInventory = player.getInventory();
                ItemStack stack = InventoryUtils.getStack(playerInventory, ItemTags.LOGS, 64);
                if (stack == null) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Inventory requirement not met. Data mismatch?");
                    return;
                }

                if (instance.getCoins(player.getUuid()) < 3000) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Coin requirement not met. Data mismatch?");
                    return;
                }

                TaleOfKingdoms.LOGGER.info("Guild rebuild requested " + playerContext + ".");
                long now = System.currentTimeMillis();
                int seconds = (int) ((now - this.lastRebuild) / 1000);
                if (seconds <= 60) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Rebuilt too recently.");
                    int secondsLeft = 60 - seconds;
                    player.sendMessage(Text.literal("The guild was only rebuilt " + seconds + " seconds ago! Please wait " + secondsLeft + " more seconds."), false);
                    return;
                }

                playerInventory.setStack(InventoryUtils.getSlotWithStack(playerInventory, stack), new ItemStack(Items.AIR));
                instance.setCoins(player.getUuid(), instance.getCoins(player.getUuid()) - 3000);
                instance.rebuild(player, api, SchematicOptions.IGNORE_DEFENDERS);
                ServerConquestInstance.sync(player, instance);
                this.lastRebuild = System.currentTimeMillis();
            });
        });
    }



    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
