package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.event.EntitySpawnCallback;
import com.convallyria.taleofkingdoms.common.item.ItemHelper;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.common.collect.ImmutableList;
import dev.architectury.registry.registries.RegistrySupplier;
import java.util.Optional;
import net.minecraft.world.entity.EntityType;

public class MobSpawnListener extends Listener {

    // Keep the suppliers and resolve at use-site: this class is constructed during mod init,
    // before NeoForge commits the deferred registrations, so calling .get() here would throw.
    private static final ImmutableList<RegistrySupplier<? extends EntityType<?>>> BLACKLIST = ImmutableList.of(
            EntityTypes.REFICULE_MAGE,
            EntityTypes.REFICULE_GUARDIAN,
            EntityTypes.REFICULE_SOLDIER);

    public MobSpawnListener() {
        EntitySpawnCallback.EVENT.register(entity -> {
            if (BLACKLIST.stream().noneMatch(supplier -> supplier.get() == entity.getType()) && ItemHelper.isHostileEntity(entity)) {
                Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance();
                return instance.isEmpty() || !instance.get().isInGuild(entity);
            }
            return true;
        });
    }
}
