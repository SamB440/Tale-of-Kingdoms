package com.convallyria.taleofkingdoms.common.entity.kingdom.warden;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class WarriorHireableEntity extends WardenHireable {

    public WarriorHireableEntity(@NotNull EntityType<? extends PathfinderMob> entityType, @NotNull Level world) {
        super(entityType, world);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    public WarriorLevel getLevel() {
        WarriorLevel highest = WarriorLevel.WARRIOR;
        for (WarriorLevel warriorLevel : WarriorLevel.values()) {
            if (getExperiencePoints() >= warriorLevel.getLevelRequired() && warriorLevel.getLevelRequired() > highest.getLevelRequired()) {
                highest = warriorLevel;
            }
        }
        return highest;
    }

    private WarriorLevel current = WarriorLevel.WARRIOR;

    @Override
    protected boolean tryLevelUp() {
        final WarriorLevel newLevel = getLevel();
        final boolean changed = current != newLevel;
        current = newLevel;
        return changed;
    }

    @Override
    public Optional<ResourceLocation> getSkin() {
        final WarriorLevel level = getLevel();
        return Optional.of(
            switch (level) {
                case WARRIOR -> identifier("textures/entity/updated_textures/warrior.png");
                case KNIGHT -> identifier("textures/entity/updated_textures/knight.png");
                case PALADIN -> identifier("textures/entity/updated_textures/paladin.png");
            }
        );
    }

    @Override
    public Component getFollowText() {
        return Component.translatable("entity_type.taleofkingdoms.warrior.follow", StringUtils.capitalize(getLevel().name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public Component getGuardText() {
        return Component.translatable("entity_type.taleofkingdoms.warrior.guard", StringUtils.capitalize(getLevel().name().toLowerCase(Locale.ROOT)));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.6D, false));
        super.registerGoals();
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    public void updateLevelledAttributes() {
        final AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        attackDamage.setBaseValue(7f + (2f * getLevel().ordinal()));
        this.current = getLevel();
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.taleofkingdoms." + getLevel().name().toLowerCase(Locale.ROOT));
    }

    public enum WarriorLevel {
        WARRIOR(0),
        KNIGHT(5),
        PALADIN(15);

        private final int levelRequired;

        WarriorLevel(int levelRequired) {
            this.levelRequired = levelRequired;
        }

        public int getLevelRequired() {
            return levelRequired;
        }
    }
}
