package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.item.ItemHelper;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MobSpawnListener extends Listener {

    @SubscribeEvent
    public void onSpawn(LivingSpawnEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (ItemHelper.isHostileEntity(entity)) {
            ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName()).get();
            if (instance.isInGuild(entity)) {
                event.setCanceled(true);
            }
        }
    }
}
