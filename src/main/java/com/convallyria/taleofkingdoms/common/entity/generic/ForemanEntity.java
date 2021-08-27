package com.convallyria.taleofkingdoms.common.entity.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.quest.Quest;
import com.convallyria.taleofkingdoms.quest.guild_captain.MiningVillage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ForemanEntity extends TOKEntity {

    public static final Identifier[] SKINS = new Identifier[]{
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/foreman/tok_foreman_0.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/foreman/tok_foreman_1.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/foreman/tok_foreman_2.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/foreman/tok_foreman_3.png")
    };

    public ForemanEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND) return ActionResult.FAIL;
        TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();

        if (player instanceof ServerPlayerEntity) return ActionResult.FAIL;
        for (Quest quest : api.getQuests()) {
            if (quest instanceof MiningVillage && quest.isTracked(player.getUuid())) {
                player.sendMessage(new LiteralText("My king! The mine has been overrun by bandits. They have slaughtered my miners. Please help me and I will reward you greatly!"), false);
                return ActionResult.PASS;
            }
        }
        if (instance instanceof ClientConquestInstance clientConquestInstance) this.openScreen(player, clientConquestInstance);
        return ActionResult.PASS;
    }

    @Environment(EnvType.CLIENT)
    private void openScreen(PlayerEntity player, ClientConquestInstance instance) {
        //todo: foreman screen
        //GuildMasterScreen screen = new GuildMasterScreen(player, this, instance);
        //MinecraftClient.getInstance().setScreen(screen);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}