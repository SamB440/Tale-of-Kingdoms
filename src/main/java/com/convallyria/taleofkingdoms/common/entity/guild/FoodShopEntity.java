package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.FoodShopScreen;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FoodShopEntity extends TOKEntity {

    public FoodShopEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 10.0F, 100F));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND || !player.level.isClientSide()) return InteractionResult.FAIL;
        ClientConquestInstance instance = (ClientConquestInstance) TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        this.openScreen(player, instance);
        return InteractionResult.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen(Player player, ClientConquestInstance instance) {
        FoodShopScreen screen = new FoodShopScreen(player, this, instance);
        Minecraft.getInstance().setScreen(screen);
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public static ImmutableList<ShopItem> getFoodShopItems() {
        return ImmutableList.copyOf(ShopParser.guiShopItems.get(ShopParser.GUI.FOOD));
    }
}
