package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class ServerSchematicHandler extends SchematicHandler {

    @Override
    @NotNull
    public CompletableFuture<BlockBox> pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockPos position, SchematicOptions... options) {
        CompletableFuture<BlockBox> cf = new CompletableFuture<>();

        // WorldEdit requires actions to be done on the server thread.
        TaleOfKingdoms.getAPI().flatMap(TaleOfKingdomsAPI::getServer).ifPresent(minecraftServer -> {
            minecraftServer.execute(() -> {
                pasteSchematic(schematic, player, position, cf, options);
            });
        });
        return cf;
    }
}
