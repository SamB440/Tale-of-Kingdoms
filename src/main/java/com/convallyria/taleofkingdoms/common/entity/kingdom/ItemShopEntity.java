package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.s2c.OpenScreenPacket;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemShopEntity extends ShopEntity {

    public ItemShopEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ShopParser.GUI getGUIType() {
        return ShopParser.GUI.ITEM;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || player.getWorld().isClient()) return ActionResult.FAIL;
        TaleOfKingdoms.getAPI().getServerPacket(Packets.OPEN_CLIENT_SCREEN).sendPacket(player, new OpenScreenPacket(OpenScreenPacket.ScreenTypes.ITEM_SHOP, this.getId()));
        return ActionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
