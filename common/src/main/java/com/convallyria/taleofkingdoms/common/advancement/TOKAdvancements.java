package com.convallyria.taleofkingdoms.common.advancement;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * Identifiers for the mod's advancements and a helper for granting the code-driven ones.
 *
 * <p>Discovery advancements (the guild, gateways, bandit camps and reficule villages) are
 * awarded declaratively by vanilla's {@code minecraft:location} trigger and never appear here.
 * The advancements listed below are milestones the game only knows about from server logic, so
 * their JSON uses the {@code minecraft:impossible} trigger and they are granted from code via
 * {@link #award(ServerPlayer, ResourceLocation)}.</p>
 */
public final class TOKAdvancements {

    // Discovery (location-triggered) — referenced here only so the id lives in one place.
    public static final ResourceLocation ROOT = id("root");

    // Code-granted milestones.
    public static final ResourceLocation SIGN_CONTRACT = id("sign_contract");
    public static final ResourceLocation PROVEN_WORTHY = id("proven_worthy");
    public static final ResourceLocation REBUILD_GUILD = id("rebuild_guild");
    public static final ResourceLocation HIRE_HUNTER = id("hire_hunter");
    public static final ResourceLocation SLAY_REFICULE_MAGE = id("slay_reficule_mage");
    public static final ResourceLocation BUILD_KINGDOM = id("build_kingdom");
    public static final ResourceLocation ROYALTY = id("royalty");
    public static final ResourceLocation HIRE_WORKER = id("hire_worker");

    private TOKAdvancements() {
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, path);
    }

    /**
     * Grants the given advancement to a player, completing every one of its criteria.
     * Safe to call repeatedly: vanilla ignores criteria that are already satisfied, so a
     * previously-earned advancement is untouched. No-op if the advancement is unknown or the
     * player is not attached to a running server (e.g. called on the logical client).
     *
     * @param player the player to award
     * @param id     the advancement identifier, e.g. {@link #SIGN_CONTRACT}
     */
    public static void award(ServerPlayer player, ResourceLocation id) {
        final MinecraftServer server = player.getServer();
        if (server == null) return;

        final AdvancementHolder holder = server.getAdvancements().get(id);
        if (holder == null) {
            TaleOfKingdoms.LOGGER.warn("Tried to award unknown advancement {}", id);
            return;
        }

        final PlayerAdvancements advancements = player.getAdvancements();
        for (String criterion : holder.value().criteria().keySet()) {
            advancements.award(holder, criterion);
        }
    }

    /**
     * Awards an advancement from code that runs on the logical client, resolving the matching
     * {@link ServerPlayer} on the server thread first.
     *
     * <p>Several of the mod's GUIs mutate shared conquest state directly in singleplayer rather
     * than round-tripping a packet, so the server-side packet handlers never run there. Calling
     * this from those GUI actions grants the advancement on the integrated server. On a multiplayer
     * client there is no local server, so this is a no-op and the (dedicated) server packet handler
     * remains responsible for the award.</p>
     *
     * @param player the acting player (client- or server-side)
     * @param id     the advancement identifier
     */
    public static void awardOnServer(Player player, ResourceLocation id) {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return;

        final UUID uuid = player.getUUID();
        api.executeOnServerEnvironment(server -> {
            if (server == null) return;
            final ServerPlayer serverPlayer = server.getPlayerList().getPlayer(uuid);
            if (serverPlayer != null) award(serverPlayer, id);
        });
    }
}
