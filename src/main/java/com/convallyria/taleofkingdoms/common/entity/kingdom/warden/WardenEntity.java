package com.convallyria.taleofkingdoms.common.entity.kingdom.warden;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.packet.outgoing.OutgoingOpenScreenPacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class WardenEntity extends TOKEntity {

    public WardenEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    public void buySoldier(PlayerEntity player, ConquestInstance instance, byte index) {
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        final int coins = guildPlayer.getCoins();
        if (coins < 1000) return;

        final PlayerKingdom kingdom = guildPlayer.getKingdom();
        if (kingdom == null) return;

        guildPlayer.setCoins(coins - 1000);

        EntityType<? extends WardenHireable> type = index == 1 ? EntityTypes.WARRIOR : EntityTypes.ARCHER; // TODO
        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> {
            ServerPlayerEntity serverPlayerEntity = player instanceof ServerPlayerEntity ? (ServerPlayerEntity) player
                    : server.getPlayerManager().getPlayer(player.getUuid());
            if (serverPlayerEntity == null) return;
            EntityUtils.spawnEntity(type, serverPlayerEntity, this.getBlockPos());
        });
    }

    public void recallSoldiers(PlayerEntity player, ConquestInstance instance) {
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        final PlayerKingdom kingdom = guildPlayer.getKingdom();
        if (kingdom == null) return;

        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> {
            ServerPlayerEntity serverPlayerEntity = player instanceof ServerPlayerEntity ? (ServerPlayerEntity) player
                    : server.getPlayerManager().getPlayer(player.getUuid());
            for (Entity entity : serverPlayerEntity.getServerWorld().iterateEntities()) {
                if (!(entity instanceof WardenHireable wardenHireable)) continue;
                entity.requestTeleport(this.getX(), this.getY(), this.getZ());
                if (!wardenHireable.isFollowingPlayer()) wardenHireable.toggleFollowGoal(serverPlayerEntity);
            }
        });
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return ActionResult.FAIL;
        if (api.getConquestInstanceStorage().mostRecentInstance().isEmpty()) return ActionResult.FAIL;
        if (hand == Hand.OFF_HAND || player.getWorld().isClient()) return ActionResult.FAIL;
        TaleOfKingdoms.getAPI().getPacketHandler(Packets.OPEN_CLIENT_SCREEN).handleOutgoingPacket(player, OutgoingOpenScreenPacketHandler.ScreenTypes.WARDEN, this.getId());
        return ActionResult.PASS;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean damage(DamageSource damageSource, float f) {
        return false;
    }
}
