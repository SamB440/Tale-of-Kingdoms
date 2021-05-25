package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.BlacksmithScreen;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.shop.ArrowShopItem;
import com.convallyria.taleofkingdoms.common.shop.BowShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondAxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondBootsShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondChestplateShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondHelmetShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondLeggingsShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondPickaxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondShovelShopItem;
import com.convallyria.taleofkingdoms.common.shop.DiamondSwordShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronAxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronBootsShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronChestplateShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronHelmetShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronLeggingsShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronPickaxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronShovelShopItem;
import com.convallyria.taleofkingdoms.common.shop.IronSwordShopItem;
import com.convallyria.taleofkingdoms.common.shop.LeatherBootsShopItem;
import com.convallyria.taleofkingdoms.common.shop.LeatherChestplateShopItem;
import com.convallyria.taleofkingdoms.common.shop.LeatherHelmetShopItem;
import com.convallyria.taleofkingdoms.common.shop.LeatherLeggingsShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShieldShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.StoneAxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.StonePickaxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.StoneShovelShopItem;
import com.convallyria.taleofkingdoms.common.shop.StoneSwordShopItem;
import com.convallyria.taleofkingdoms.common.shop.WoodenAxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.WoodenPickaxeShopItem;
import com.convallyria.taleofkingdoms.common.shop.WoodenShovelShopItem;
import com.convallyria.taleofkingdoms.common.shop.WoodenSwordShopItem;
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

    public static ImmutableList<ShopItem> getBlacksmithShopItems() {
        return ImmutableList.of(new ArrowShopItem(), new BowShopItem(),
                new DiamondAxeShopItem(), new DiamondBootsShopItem(),
                new DiamondChestplateShopItem(), new DiamondHelmetShopItem(), new DiamondLeggingsShopItem(), new DiamondPickaxeShopItem(),
                new DiamondShovelShopItem(), new DiamondSwordShopItem(),
                new IronAxeShopItem(), new IronBootsShopItem(), new IronChestplateShopItem(), new IronHelmetShopItem(),
                new IronLeggingsShopItem(), new IronPickaxeShopItem(), new IronShovelShopItem(), new IronSwordShopItem(),
                new LeatherBootsShopItem(), new LeatherChestplateShopItem(), new LeatherHelmetShopItem(), new LeatherLeggingsShopItem(),
                new ShieldShopItem(), new StoneAxeShopItem(), new StonePickaxeShopItem(), new StoneShovelShopItem(), new StoneSwordShopItem(),
                new WoodenAxeShopItem(), new WoodenPickaxeShopItem(), new WoodenShovelShopItem(), new WoodenSwordShopItem());
    }
}
