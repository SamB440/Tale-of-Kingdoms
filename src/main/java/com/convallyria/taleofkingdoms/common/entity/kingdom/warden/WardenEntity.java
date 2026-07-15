package com.convallyria.taleofkingdoms.common.entity.kingdom.warden;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.s2c.OpenScreenPacket;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WardenEntity extends TOKEntity {

    public WardenEntity(@NotNull EntityType<? extends PathfinderMob> entityType, @NotNull Level world) {
        super(entityType, world);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    public void buySoldier(Player player, ConquestInstance instance, byte index) {
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        final int coins = guildPlayer.getCoins();
        if (coins < 1000) return;

        final PlayerKingdom kingdom = guildPlayer.getKingdom();
        if (kingdom == null) return;

        guildPlayer.setCoins(coins - 1000);

        EntityType<? extends WardenHireable> type = index == 1 ? EntityTypes.WARRIOR : EntityTypes.ARCHER; // TODO
        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> {
            ServerPlayer serverPlayerEntity = player instanceof ServerPlayer ? (ServerPlayer) player
                    : server.getPlayerList().getPlayer(player.getUUID());
            if (serverPlayerEntity == null) return;
            final WardenHireable wardenHireable = EntityUtils.spawnEntity(type, serverPlayerEntity, this.blockPosition());
            wardenHireable.toggleFollowGoal(player);
        });
    }

    public void recallSoldiers(Player player, ConquestInstance instance) {
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        final PlayerKingdom kingdom = guildPlayer.getKingdom();
        if (kingdom == null) return;

        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> {
            ServerPlayer serverPlayerEntity = player instanceof ServerPlayer ? (ServerPlayer) player
                    : server.getPlayerList().getPlayer(player.getUUID());
            for (Entity entity : serverPlayerEntity.serverLevel().getAllEntities()) {
                if (!(entity instanceof WardenHireable wardenHireable)) continue;
                entity.teleportTo(this.getX(), this.getY(), this.getZ());
                if (!wardenHireable.isFollowingPlayer()) wardenHireable.toggleFollowGoal(serverPlayerEntity);
            }
        });
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return InteractionResult.FAIL;
        if (api.getConquestInstanceStorage().mostRecentInstance().isEmpty()) return InteractionResult.FAIL;
        if (hand == InteractionHand.OFF_HAND || player.level().isClientSide()) return InteractionResult.FAIL;
        TaleOfKingdoms.getAPI().getServerPacket(Packets.OPEN_CLIENT_SCREEN).sendPacket(player, new OpenScreenPacket(OpenScreenPacket.ScreenTypes.WARDEN, this.getId()));
        return InteractionResult.PASS;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 10.0F, 100F));
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        return false;
    }
}
