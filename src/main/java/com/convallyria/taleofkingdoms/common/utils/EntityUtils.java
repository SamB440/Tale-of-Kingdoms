package com.convallyria.taleofkingdoms.common.utils;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;

public class EntityUtils {

    public static MobEntity spawnEntity(EntityType<?> type, ServerPlayerEntity serverPlayer, BlockPos pos) {
        return spawnEntity(type, serverPlayer.getServerWorld(), pos);
    }

    public static MobEntity spawnEntity(EntityType<?> type, ServerWorldAccess serverWorldAccess, BlockPos pos) {
        MobEntity entity = (MobEntity) type.create(serverWorldAccess.toServerWorld());
        if (entity == null) return null;
        entity.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        entity.initialize(serverWorldAccess, serverWorldAccess.getLocalDifficulty(pos), SpawnReason.STRUCTURE, null, null);
        serverWorldAccess.spawnEntityAndPassengers(entity);
        TaleOfKingdoms.LOGGER.info("Spawned entity " + entity.getEntityName() + " " + entity + " " + entity.getX() + "," + entity.getY() + "," + entity.getZ());
        return entity;
    }
}
