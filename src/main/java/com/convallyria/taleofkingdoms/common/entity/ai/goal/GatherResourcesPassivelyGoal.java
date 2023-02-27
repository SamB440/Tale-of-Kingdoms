package com.convallyria.taleofkingdoms.common.entity.ai.goal;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.kingdom.QuarryForemanEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.concurrent.ThreadLocalRandom;

public class GatherResourcesPassivelyGoal extends Goal {

    private final MobEntity mob;

    public GatherResourcesPassivelyGoal(MobEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return ThreadLocalRandom.current().nextInt(100) > 75;
    }

    @Override
    public void tick() {
        mob.swingHand(mob.getActiveHand(), true); //todo: this doesn't work and i don't know why
        if (ThreadLocalRandom.current().nextInt(100) > 98) {
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                for (PlayerKingdom kingdom : instance.getKingdoms()) {
                    if (kingdom.isInKingdom(mob.getBlockPos())) {
                        kingdom.getKingdomEntity(mob.world, EntityTypes.QUARRY_FOREMAN).ifPresent(quarryForeman -> {
                            quarryForeman.getInventory().addStack(new ItemStack(Items.COBBLESTONE, 1));
                            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                                updateClient(mob, quarryForeman);
                            }
                        });
                    }
                }
            });
        }
    }

    @Environment(EnvType.CLIENT)
    private void updateClient(MobEntity serverQuarryWorker, QuarryForemanEntity serverQuarryForeman) {
        final Entity quarryForemanById = MinecraftClient.getInstance().player.world.getEntityById(serverQuarryForeman.getId());
        if (!(quarryForemanById instanceof QuarryForemanEntity quarryForemanEntity)) return;
        quarryForemanEntity.getInventory().addStack(new ItemStack(Items.COBBLESTONE, 1));
    }
}
