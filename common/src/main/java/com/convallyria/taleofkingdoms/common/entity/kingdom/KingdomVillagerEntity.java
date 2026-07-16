package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundKingdomGoal;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class KingdomVillagerEntity extends TOKEntity {

    private static final List<ResourceLocation> VALID_SKINS = List.of(
            // So much for 50/50 gender distribution.
            identifier("textures/entity/updated_textures/woman1.png"),
            identifier("textures/entity/updated_textures/woman2.png"),
            identifier("textures/entity/updated_textures/manone.png"),
            identifier("textures/entity/updated_textures/mantwo.png"),
            identifier("textures/entity/updated_textures/manthree.png"),
            identifier("textures/entity/updated_textures/manfour.png"),
            identifier("textures/entity/updated_textures/manfive.png"),
            identifier("textures/entity/updated_textures/mansix.png")
    );

    private final ResourceLocation skin;

    public KingdomVillagerEntity(@NotNull EntityType<? extends PathfinderMob> entityType, @NotNull Level world) {
        super(entityType, world);
        this.skin = VALID_SKINS.get(ThreadLocalRandom.current().nextInt(VALID_SKINS.size()));
    }

    @Override
    public Optional<ResourceLocation> getSkin() {
        return Optional.of(skin);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new WanderAroundKingdomGoal(this, 0.6D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 5.0F, 60F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND || player.level().isClientSide) return InteractionResult.FAIL;

        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return InteractionResult.FAIL;
        if (api.getConquestInstanceStorage().mostRecentInstance().isEmpty()) return InteractionResult.FAIL;

        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        if (guildPlayer.getKingdom() != null) {
            //todo: convert to worker if items are given
            Translations.VILLAGER_ASK_TO_WORK.send(player, guildPlayer.getTitle().getTranslation());
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
