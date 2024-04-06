package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.server.packet.outgoing.OutgoingOpenScreenPacketHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockShopEntity extends ShopEntity {

    public BlockShopEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || player.getWorld().isClient()) return ActionResult.FAIL;
        TaleOfKingdoms.getAPI().getPacketHandler(Packets.OPEN_CLIENT_SCREEN).handleOutgoingPacket(player, OutgoingOpenScreenPacketHandler.ScreenTypes.BLOCK_SHOP, this.getId());
        return ActionResult.PASS;
    }

    @Override
    public ShopParser.GUI getGUIType() {
        return ShopParser.GUI.BLOCK;
    }

    @Override
    public boolean isStationary() {
        return true;
    }
}
