package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
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
        if (hand == Hand.OFF_HAND) return ActionResult.FAIL;

        // Check if there is at least 1 Minecraft day difference
        if (!TaleOfKingdoms.getAPI().isPresent()) return ActionResult.FAIL;
        TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
        if (!api.getConquestInstanceStorage().mostRecentInstance().isPresent()) return ActionResult.FAIL;

        if (player.getServer() != null && !player.getServer().isDedicated()) {
            ClientConquestInstance instance = (ClientConquestInstance) api.getConquestInstanceStorage().mostRecentInstance().get();
            long day = player.world.getTimeOfDay() / 24000L;
            if (instance.getFarmerLastBread() >= day) {
                Translations.FARMER_GOT_BREAD.send(player);
                return ActionResult.FAIL;
            }

            // Set the current day and add bread to inventory
            instance.setFarmerLastBread(day);
            Translations.FARMER_TAKE_BREAD.send(player);
        } else if (player.getServer() != null && player.getServer().isDedicated()) {
            ServerConquestInstance instance = (ServerConquestInstance) api.getConquestInstanceStorage().mostRecentInstance().get();
            long day = player.world.getTimeOfDay() / 24000L;
            if (instance.getFarmerLastBread(player.getUuid()) >= day) {
                Translations.FARMER_GOT_BREAD.send(player);
                return ActionResult.FAIL;
            }

            // Set the current day and add bread to inventory
            instance.setFarmerLastBread(player.getUuid(), day);
            Translations.FARMER_TAKE_BREAD.send(player);
        }

        int amount = ThreadLocalRandom.current().nextInt(1, 4);
        if (player.getServer() != null && !player.getServer().isDedicated()) {
            api.executeOnMain(() -> {
                MinecraftServer server = player.getServer();
                if (server != null) {
                    ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(player.getUuid());
                    if (serverPlayerEntity != null) {
                        serverPlayerEntity.inventory.insertStack(new ItemStack(Items.BREAD, amount));
                    }
                }
            });
        } else if (player.getServer() != null && player.getServer().isDedicated()) {
            api.executeOnDedicatedServer(() -> player.inventory.insertStack(new ItemStack(Items.BREAD, amount)));
        }
        return ActionResult.PASS;
    }
}
