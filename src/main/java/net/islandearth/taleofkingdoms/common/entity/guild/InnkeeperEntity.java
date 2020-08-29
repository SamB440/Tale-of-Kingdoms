package net.islandearth.taleofkingdoms.common.entity.guild;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.entity.InnkeeperScreen;
import net.islandearth.taleofkingdoms.client.translation.Translations;
import net.islandearth.taleofkingdoms.common.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class InnkeeperEntity extends TOKEntity {

    public InnkeeperEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 15.0F, 100F));
        applyEntityAI();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || !player.world.isClient()) return ActionResult.FAIL;
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        if (!instance.hasContract()) {
            Translations.NEED_CONTRACT.send(player);
            return ActionResult.FAIL;
        }

        InnkeeperScreen screen = new InnkeeperScreen(player, this, instance);
        MinecraftClient.getInstance().openScreen(screen);
        return ActionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return true;
    }
}
