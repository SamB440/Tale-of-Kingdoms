package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundKingdomGoal;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class KingdomVillagerEntity extends TOKEntity {

    public KingdomVillagerEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
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
        if (hand == Hand.OFF_HAND || player.world.isClient) return ActionResult.FAIL;

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
