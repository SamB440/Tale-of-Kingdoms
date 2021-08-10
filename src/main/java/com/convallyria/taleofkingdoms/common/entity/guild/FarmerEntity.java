package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class FarmerEntity extends TOKEntity {

    public FarmerEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_HOE));
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
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND || player.level.isClientSide) return InteractionResult.FAIL;

        // Check if there is at least 1 Minecraft day difference
        if (TaleOfKingdoms.getAPI().isEmpty()) return InteractionResult.FAIL;
        TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
        if (api.getConquestInstanceStorage().mostRecentInstance().isEmpty()) return InteractionResult.FAIL;

        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        UUID uuid = player.getUUID();
        long day = player.level.getDayTime() / 24000L;
        if (instance.getFarmerLastBread(uuid) >= day) {
            Translations.FARMER_GOT_BREAD.send(player);
            return InteractionResult.FAIL;
        }

        // Set the current day and add bread to inventory
        instance.setFarmerLastBread(uuid, day);
        Translations.FARMER_TAKE_BREAD.send(player);

        int amount = ThreadLocalRandom.current().nextInt(1, 4);
        if (instance instanceof ClientConquestInstance) {
            api.executeOnMain(() -> {
                MinecraftServer server = player.getServer();
                if (server != null) {
                    ServerPlayer serverPlayerEntity = server.getPlayerList().getPlayer(player.getUUID());
                    if (serverPlayerEntity != null) {
                        serverPlayerEntity.getInventory().add(new ItemStack(Items.BREAD, amount));
                    }
                }
            });
        } else if (instance instanceof ServerConquestInstance) {
            api.executeOnDedicatedServer(() -> player.getInventory().add(new ItemStack(Items.BREAD, amount)));
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
