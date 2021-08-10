package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.FollowPlayerGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.HealPlayerGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Optional;

public class GuildMasterDefenderEntity extends GuildMasterEntity {
    private boolean givenSword;

    public GuildMasterDefenderEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(1000.0D);
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.6D, false));
        this.goalSelector.addGoal(2, new FollowPlayerGoal(this, 0.8F, 5F, 15F));
        this.goalSelector.addGoal(3, new HealPlayerGoal(this, 10F));
        this.targetSelector.addGoal(1, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_SOLDIER, false));
        this.targetSelector.addGoal(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_GUARDIAN, false));
        this.targetSelector.addGoal(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_MAGE, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 100,
                true, true, livingEntity -> livingEntity instanceof Enemy));
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    public boolean fireImmune() {
        if (TaleOfKingdoms.getAPI().isPresent()) {
            Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance();
            if (instance.isPresent()) {
                return instance.get().isUnderAttack();
            }
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (TaleOfKingdoms.getAPI().isPresent()) {
            if (TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().isPresent()) {
                ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
                if (instance.isUnderAttack() && !instance.hasRebuilt()) {
                    return false;
                }
            }
        }
        return super.hurt(damageSource, f);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == Hand.OFF_HAND || !(player instanceof ServerPlayerEntity serverPlayerEntity)) return ActionResult.FAIL;
        TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        if (instance.isUnderAttack()) {
            if (!givenSword && !player.getInventory().containsAny(new HashSet<>(FabricToolTags.SWORDS.values()))) { // Use containsAny method as it is present on both server and client
                Runnable giveItem = () -> {
                    MinecraftServer server = player.getServer();
                    if (server != null) {
                        serverPlayerEntity.getInventory().insertStack(new ItemStack(Items.IRON_SWORD));
                        this.givenSword = true;
                    }
                };
                if (instance instanceof ServerConquestInstance) api.executeOnDedicatedServer(giveItem);
                else api.executeOnMain(giveItem);
                return ActionResult.SUCCESS;
            }

            if (instance.getReficuleAttackers().size() == 0) {
                if (!instance.hasRebuilt()) {
                    Runnable fixGuild = () -> {
                        PlayerInventory playerInventory = serverPlayerEntity.getInventory();
                        ItemStack stack = null;
                        for (ItemStack itemStack : playerInventory.main) {
                            if (ItemTags.LOGS.values().contains(itemStack.getItem())) {
                                if (itemStack.getCount() == 64) {
                                    stack = itemStack;
                                    break;
                                }
                            }
                        }

                        if (stack != null) {
                            playerInventory.setStack(InventoryUtils.getSlotWithStack(playerInventory, stack), new ItemStack(Items.AIR));
                            instance.rebuild(serverPlayerEntity, api);
                            instance.setRebuilt(true);
                            instance.setUnderAttack(false);
                            serverPlayerEntity.getServerWorld().getEntityById(this.getId()).kill();
                            Translations.GUILDMASTER_THANK_YOU.send(player);
                        } else {
                            Translations.GUILDMASTER_REBUILD.send(player);
                        }
                    };
                    if (instance instanceof ServerConquestInstance) api.executeOnDedicatedServer(fixGuild);
                    else if (serverPlayerEntity.getServer() == null || !serverPlayerEntity.getServer().isDedicated()) api.executeOnMain(fixGuild);
                    return ActionResult.SUCCESS;
                }
            } else if (instance.getReficuleAttackers().size() <= 4) {
                Translations.GUILDMASTER_KILL_REFICULES.send(player);
            } else {
                Translations.GUILDMASTER_STAY_CLOSE.send(player);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public CompoundTag saveWithoutId(CompoundTag tag) {
        tag.putBoolean("givenSword", givenSword);
        return super.saveWithoutId(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        this.givenSword = tag.getBoolean("givenSword");
        super.load(tag);
    }
}
