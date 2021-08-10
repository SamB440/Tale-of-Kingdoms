package com.convallyria.taleofkingdoms.common.utils;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class EntityUtils {

    @Nullable
    public static <T extends Mob> T spawnEntity(EntityType<T> type, ServerPlayer serverPlayer, BlockPos pos) {
        return spawnEntity(type, serverPlayer.getLevel(), pos);
    }

    @Nullable
    public static <T extends Mob> T spawnEntity(EntityType<T> type, ServerLevelAccessor serverWorldAccess, BlockPos pos) {
        T entity = type.create(serverWorldAccess.getLevel());
        if (entity == null) return null;
        entity.moveTo(pos, 0.0F, 0.0F);
        entity.finalizeSpawn(serverWorldAccess, serverWorldAccess.getCurrentDifficultyAt(pos), MobSpawnType.COMMAND, null, null);
        serverWorldAccess.addFreshEntityWithPassengers(entity);
        TaleOfKingdoms.LOGGER.debug("Spawned entity " + entity.getScoreboardName() + " " + entity + " " + entity.getX() + "," + entity.getY() + "," + entity.getZ());
        return entity;
    }
}
