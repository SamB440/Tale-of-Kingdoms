package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class CommonSchematicHandler extends SchematicHandler {

    @Override
    @NotNull
    public CompletableFuture<BlockBox> pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockPos position, BlockRotation rotation, SchematicOptions... options) {
        CompletableFuture<BlockBox> cf = new CompletableFuture<>();

        // If on:
        // Dedicated Server --> WorldEdit requires actions to be done on the dedicated server thread.
        // Client --> WorldEdit requires actions to be done on the client server thread.
        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> pasteSchematic(schematic, player, position, rotation, cf, options));
        return cf;
    }
}
