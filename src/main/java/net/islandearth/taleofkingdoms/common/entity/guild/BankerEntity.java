package net.islandearth.taleofkingdoms.common.entity.guild;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.entity.BankerScreen;
import net.islandearth.taleofkingdoms.common.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.world.ClientConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BankerEntity extends TOKEntity {

    public BankerEntity(EntityType<? extends PathAwareEntity> entityType, World world) { super(entityType, world); }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
        applyEntityAI();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || !player.world.isClient()) return ActionResult.FAIL;
        ClientConquestInstance instance = (ClientConquestInstance) TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        this.openScreen(player, instance);
        return ActionResult.PASS;
    }

    @Environment(EnvType.CLIENT)
    private void openScreen(PlayerEntity player, ClientConquestInstance instance) {
        BankerScreen screen = new BankerScreen(player, this, instance);
        MinecraftClient.getInstance().openScreen(screen);
    }

    @Override
    public boolean isStationary() { return true; }
}
