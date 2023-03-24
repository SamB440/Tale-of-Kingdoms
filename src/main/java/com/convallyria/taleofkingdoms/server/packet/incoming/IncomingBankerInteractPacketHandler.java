package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class IncomingBankerInteractPacketHandler extends ServerPacketHandler {

    public IncomingBankerInteractPacketHandler() {
        super(Packets.BANKER_INTERACT);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context;
        UUID uuid = player.getUuid();
        BankerMethod method = attachedData.readEnumConstant(BankerMethod.class);
        int coins = attachedData.readInt();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            if (!instance.isInGuild(player)) {
                reject(player, "Not in guild.");
                return;
            }

            // Search for banker
            Optional<BankerEntity> entity = instance.getGuildEntity(player.world, EntityTypes.BANKER);
            if (entity.isEmpty()) {
                reject(player, "Banker entity not present in guild.");
                return;
            }

            final GuildPlayer guildPlayer = instance.getPlayer(uuid);
            if (guildPlayer.getCoins() == 0 && guildPlayer.getBankerCoins() == 0) {
                reject(player, "No coins.");
                return;
            }

            if (method == BankerMethod.DEPOSIT) {
                if (guildPlayer.getCoins() < coins) {
                    reject(player, "Not enough coins.");
                    return;
                }
                guildPlayer.setCoins(guildPlayer.getCoins() - coins);
                guildPlayer.setBankerCoins(guildPlayer.getBankerCoins() + coins);
            } else {
                if (guildPlayer.getBankerCoins() < coins) {
                    reject(player, "Not enough coins.");
                    return;
                }
                guildPlayer.setBankerCoins(guildPlayer.getBankerCoins() - coins);
                instance.addCoins(uuid, coins);
            }
            ServerConquestInstance.sync(player, instance);
        }));
    }

    @Override
    public void handleOutgoingPacket(@NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
