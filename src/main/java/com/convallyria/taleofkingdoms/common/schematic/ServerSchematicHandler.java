package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class ServerSchematicHandler extends SchematicHandler {

    @Override
    @NotNull
    public CompletableFuture<OperationInstance> pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockVector3 position) {
        CompletableFuture<OperationInstance> cf = new CompletableFuture<>();

        // WorldEdit requires actions to be done on the server thread.
        TaleOfKingdoms.getAPI().flatMap(TaleOfKingdomsAPI::getServer).ifPresent(minecraftServer -> {
            minecraftServer.execute(() -> {
                pasteSchematic(schematic, player, position, cf);
            });
        });
        return cf;
    }
}
