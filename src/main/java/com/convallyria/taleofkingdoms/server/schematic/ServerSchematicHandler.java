package com.convallyria.taleofkingdoms.server.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.schematic.SchematicHandler;
import com.convallyria.taleofkingdoms.common.schematic.SchematicOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@Environment(EnvType.SERVER)
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
