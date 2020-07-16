package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.item.ItemHelper;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class MobSpawnListener extends Listener {

    @SubscribeEvent
    public void onSpawn(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (ItemHelper.isHostileEntity(entity)) {
            if (Minecraft.getInstance().getIntegratedServer() == null) return;
            Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName());
            if (instance.isPresent() && instance.get().isInGuild(entity)) {
                event.setCanceled(true);
            }
        }
    }
}
