package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class CommonSchematicHandler extends SchematicHandler {

    @Override
    @NotNull
    public CompletableFuture<BlockBox> pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockPos position, SchematicOptions... options) {
        CompletableFuture<BlockBox> cf = new CompletableFuture<>();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            // WorldEdit requires actions to be done on the dedicated server thread.
            TaleOfKingdoms.getAPI().getServer().ifPresent(minecraftServer -> {
                minecraftServer.execute(() -> pasteSchematic(schematic, player, position, cf, options));
            });
        } else {
            // WorldEdit requires actions to be done on the client server thread.
            TaleOfKingdoms.getAPI().executeOnServer(() -> pasteSchematic(schematic, player, position, cf, options));
        }
        return cf;
    }
}
