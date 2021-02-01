package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
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

import java.util.UUID;
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
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || player.world.isClient) return ActionResult.FAIL;

        // Check if there is at least 1 Minecraft day difference
        if (!TaleOfKingdoms.getAPI().isPresent()) return ActionResult.FAIL;
        TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
        if (!api.getConquestInstanceStorage().mostRecentInstance().isPresent()) return ActionResult.FAIL;

        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        UUID uuid = null;
        long day = player.world.getTimeOfDay() / 24000L;
        if (instance.getFarmerLastBread() >= day) {
            Translations.FARMER_GOT_BREAD.send(player);
            return ActionResult.FAIL;
        }

        if (player.getServer() != null && player.getServer().isDedicated()) {
            uuid = player.getUuid();
            Translations.FARMER_TAKE_BREAD.send(player);
        }

        // Set the current day and add bread to inventory
        instance.setFarmerLastBread(uuid, day);
        Translations.FARMER_TAKE_BREAD.send(player);

        int amount = ThreadLocalRandom.current().nextInt(1, 4);
        if (instance instanceof ClientConquestInstance) {
            api.executeOnMain(() -> {
                MinecraftServer server = player.getServer();
                if (server != null) {
                    ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(player.getUuid());
                    if (serverPlayerEntity != null) {
                        serverPlayerEntity.inventory.insertStack(new ItemStack(Items.BREAD, amount));
                    }
                }
            });
        } else if (instance instanceof ServerConquestInstance) {
            api.executeOnDedicatedServer(() -> player.inventory.insertStack(new ItemStack(Items.BREAD, amount)));
        }
        return ActionResult.PASS;
    }
}
