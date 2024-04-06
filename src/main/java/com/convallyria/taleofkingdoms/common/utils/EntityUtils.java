package com.convallyria.taleofkingdoms.common.utils;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import org.jetbrains.annotations.Nullable;

public class EntityUtils {

    @Nullable
    public static <T extends MobEntity> T spawnEntity(EntityType<T> type, ServerPlayerEntity serverPlayer, BlockPos pos) {
        return spawnEntity(type, serverPlayer.getServerWorld(), pos);
    }

    @Nullable
    public static <T extends MobEntity> T spawnEntity(EntityType<T> type, ServerWorldAccess serverWorldAccess, BlockPos pos) {
        T entity = type.create(serverWorldAccess.toServerWorld());
        if (entity == null) return null;
        entity.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        entity.initialize(serverWorldAccess, serverWorldAccess.getLocalDifficulty(pos), SpawnReason.COMMAND, null, null);
        serverWorldAccess.spawnEntityAndPassengers(entity);
        TaleOfKingdoms.LOGGER.debug("Spawned entity " + entity.getType().getUntranslatedName() + " " + entity + " " + entity.getX() + "," + entity.getY() + "," + entity.getZ());
        return entity;
    }
}
