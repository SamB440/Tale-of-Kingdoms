package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.fabricmc.api.EnvType;
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
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || player.getWorld().isClient) return ActionResult.FAIL;

        // Check if there is at least 1 Minecraft day difference
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return ActionResult.FAIL;
        if (api.getConquestInstanceStorage().mostRecentInstance().isEmpty()) return ActionResult.FAIL;

        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        final GuildPlayer guildPlayer = instance.getPlayer(player);

        final long day = player.getWorld().getTimeOfDay() / 24000L;
        if (guildPlayer.getFarmerLastBread() >= day) {
            Translations.FARMER_GOT_BREAD.send(player);
            return ActionResult.FAIL;
        }

        // Set the current day and add bread to inventory
        guildPlayer.setFarmerLastBread(day);
        Translations.FARMER_TAKE_BREAD.send(player);

        int amount = ThreadLocalRandom.current().nextInt(1, 4);
        if (api.getEnvironment() == EnvType.CLIENT) {
            api.executeOnMain(() -> {
                MinecraftServer server = player.getServer();
                if (server != null) {
                    ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(player.getUuid());
                    if (serverPlayerEntity != null) {
                        serverPlayerEntity.getInventory().insertStack(new ItemStack(Items.BREAD, amount));
                    }
                }
            });
        } else {
            api.executeOnDedicatedServer(() -> player.getInventory().insertStack(new ItemStack(Items.BREAD, amount)));
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
