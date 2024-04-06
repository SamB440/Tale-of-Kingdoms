package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundKingdomGoal;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class KingdomVillagerEntity extends TOKEntity {

    private static final List<Identifier> VALID_SKINS = List.of(
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

    private final Identifier skin;

    public KingdomVillagerEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
        this.skin = VALID_SKINS.get(ThreadLocalRandom.current().nextInt(VALID_SKINS.size()));
    }

    @Override
    public Optional<Identifier> getSkin() {
        return Optional.of(skin);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new WanderAroundKingdomGoal(this, 0.6D));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 5.0F, 60F));
        this.goalSelector.add(3, new LookAroundGoal(this));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || player.getWorld().isClient) return ActionResult.FAIL;

        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return ActionResult.FAIL;
        if (api.getConquestInstanceStorage().mostRecentInstance().isEmpty()) return ActionResult.FAIL;

        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        if (guildPlayer.getKingdom() != null) {
            //todo: convert to worker if items are given
            Translations.VILLAGER_ASK_TO_WORK.send(player);
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
