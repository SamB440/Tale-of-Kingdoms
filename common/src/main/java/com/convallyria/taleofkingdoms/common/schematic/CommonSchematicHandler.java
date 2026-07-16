package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public final class CommonSchematicHandler extends SchematicHandler {

    @Override
    @NotNull
    public CompletableFuture<BoundingBox> pasteSchematic(Schematic schematic, ServerPlayer player, BlockPos position, Rotation rotation, SchematicOptions... options) {
        CompletableFuture<BoundingBox> cf = new CompletableFuture<>();

        // If on:
        // Dedicated Server --> WorldEdit requires actions to be done on the dedicated server thread.
        // Client --> WorldEdit requires actions to be done on the client server thread.
        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> pasteSchematic(schematic, player, position, rotation, cf, options));
        return cf;
    }
}
