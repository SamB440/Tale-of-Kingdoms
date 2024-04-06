package com.convallyria.taleofkingdoms.common.entity.kingdom.warden;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

public class WarriorHireableEntity extends WardenHireable {

    public WarriorHireableEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
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
    public Optional<Identifier> getSkin() {
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
    public Text getFollowText() {
        return Text.translatable("entity_type.taleofkingdoms.warrior.follow", StringUtils.capitalize(getLevel().name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public Text getGuardText() {
        return Text.translatable("entity_type.taleofkingdoms.warrior.guard", StringUtils.capitalize(getLevel().name().toLowerCase(Locale.ROOT)));
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.6D, false));
        super.initGoals();
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    public void updateLevelledAttributes() {
        final EntityAttributeInstance attackDamage = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        attackDamage.setBaseValue(7f + (2f * getLevel().ordinal()));
        this.current = getLevel();
    }

    @Override
    protected Text getDefaultName() {
        return Text.translatable("entity.taleofkingdoms." + getLevel().name().toLowerCase(Locale.ROOT));
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
