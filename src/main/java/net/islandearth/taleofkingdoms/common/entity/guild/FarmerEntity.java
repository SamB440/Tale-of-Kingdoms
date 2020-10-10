package net.islandearth.taleofkingdoms.common.entity.guild;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.translation.Translations;
import net.islandearth.taleofkingdoms.common.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public class FarmerEntity extends TOKEntity {

    public FarmerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_HOE));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
        applyEntityAI();
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || !player.world.isClient()) return ActionResult.FAIL;

        // Check if there is at least 1 Minecraft day difference
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        long day = player.world.getTimeOfDay() / 24000L;
        if (instance.getFarmerLastBread() >= day) {
            Translations.FARMER_GOT_BREAD.send(player);
            return ActionResult.FAIL;
        }

        // Set the current day and add bread to inventory
        instance.setFarmerLastBread(day);
        Translations.FARMER_TAKE_BREAD.send(player);
        int amount = ThreadLocalRandom.current().nextInt(1, 4);
        MinecraftClient.getInstance().getServer().execute(() -> {
            ServerPlayerEntity serverPlayerEntity = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(player.getUuid());
            serverPlayerEntity.inventory.insertStack(new ItemStack(Items.BREAD, amount));
        });
        return ActionResult.PASS;
    }
}
