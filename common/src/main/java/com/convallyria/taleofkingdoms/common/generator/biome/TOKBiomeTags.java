package com.convallyria.taleofkingdoms.common.generator.biome;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class TOKBiomeTags {

    public static final TagKey<Biome> SOLID_SURFACE = of("has_structure/solid_surface");
    public static final TagKey<Biome> NO_MOUNTAINS_DESERTS = of("has_structure/no_mountains_deserts");
    /**
     * Land biomes the guild's ring placement snaps towards (vanilla + optional {@code c:}
     * convention tags so modded biomes, e.g. Biomes O' Plenty, are preferred too).
     */
    public static final TagKey<Biome> GUILD = of("has_structure/guild");
    /**
     * Biomes the guild must never generate in (oceans, rivers and modded aquatic biomes).
     * Used as a code-side exclusion so untagged modded land biomes still get a guild.
     */
    public static final TagKey<Biome> GUILD_AVOID = of("guild_avoid");

    private TOKBiomeTags() {}

    private static TagKey<Biome> of(String id) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, id));
    }
}