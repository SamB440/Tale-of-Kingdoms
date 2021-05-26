package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.FoodShopScreen;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.shop.AppleShopItem;
import com.convallyria.taleofkingdoms.common.shop.BeetrootShopItem;
import com.convallyria.taleofkingdoms.common.shop.BreadShopItem;
import com.convallyria.taleofkingdoms.common.shop.CakeShopItem;
import com.convallyria.taleofkingdoms.common.shop.CarrotShopItem;
import com.convallyria.taleofkingdoms.common.shop.CookieShopItem;
import com.convallyria.taleofkingdoms.common.shop.GoldenAppleShopItem;
import com.convallyria.taleofkingdoms.common.shop.MelonShopItem;
import com.convallyria.taleofkingdoms.common.shop.PotatoShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawBeefShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawChickenShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawCodShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawMuttonShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawPorkchopShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawRabbitShopItem;
import com.convallyria.taleofkingdoms.common.shop.RawSalmonShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
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

public class FoodShopEntity extends TOKEntity {

    public FoodShopEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
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
        FoodShopScreen screen = new FoodShopScreen(player, this, instance);
        MinecraftClient.getInstance().openScreen(screen);
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
        return ImmutableList.of(new AppleShopItem(), new BeetrootShopItem(), new BreadShopItem(),
                new CakeShopItem(), new CarrotShopItem(), new CookieShopItem(), new GoldenAppleShopItem(),
                new MelonShopItem(), new PotatoShopItem(), new RawBeefShopItem(), new RawChickenShopItem(),
                new RawCodShopItem(), new RawMuttonShopItem(), new RawPorkchopShopItem(), new RawRabbitShopItem(),
                new RawSalmonShopItem());
    }
}
