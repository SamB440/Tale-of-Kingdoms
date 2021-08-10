package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class ServerSchematicHandler extends SchematicHandler {

    @Override
    @NotNull
    public CompletableFuture<BoundingBox> pasteSchematic(Schematic schematic, ServerPlayer player, BlockPos position, SchematicOptions... options) {
        CompletableFuture<BoundingBox> cf = new CompletableFuture<>();

        // WorldEdit requires actions to be done on the server thread.
        TaleOfKingdoms.getAPI().flatMap(TaleOfKingdomsAPI::getServer).ifPresent(minecraftServer -> {
            minecraftServer.execute(() -> {
                pasteSchematic(schematic, player, position, cf, options);
            });
        });
        return cf;
    }
}
