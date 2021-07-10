package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.BlacksmithScreen;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BlacksmithEntity extends TOKEntity {

    public BlacksmithEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || !player.world.isClient()) return ActionResult.FAIL;
        ClientConquestInstance instance = (ClientConquestInstance) TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        this.openScreen(player, instance);
        return ActionResult.PASS;
    }

    @Environment(EnvType.CLIENT)
    private void openScreen(PlayerEntity player, ClientConquestInstance instance) {
        BlacksmithScreen screen = new BlacksmithScreen(player, this, instance);
        MinecraftClient.getInstance().setScreen(screen);
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public static ImmutableList<ShopItem> getBlacksmithShopItems() {
        return ImmutableList.copyOf(ShopParser.guiShopItems.get(ShopParser.GUI.BLACKSMITH));
    }
}
