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
import net.fabricmc.api.EnvType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Set;

public class GuildMasterDefenderEntity extends GuildMasterEntity {
    private boolean givenSword;

    public GuildMasterDefenderEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(1000.0D);
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.6D, false));
        this.goalSelector.add(2, new FollowPlayerGoal(this, 0.8F, 5F, 15F));
        this.goalSelector.add(3, new HealPlayerGoal(this, 10F));
        this.targetSelector.add(1, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_SOLDIER, false));
        this.targetSelector.add(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_GUARDIAN, false));
        this.targetSelector.add(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_MAGE, false));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, MobEntity.class, 100,
                true, true, livingEntity -> livingEntity instanceof Monster));
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    public boolean isFireImmune() {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api != null) {
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isPresent()) {
                return instance.get().isUnderAttack();
            }
        }
        return false;
    }

    @Override
    public boolean damage(DamageSource damageSource, float f) {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api != null) {
            if (api.getConquestInstanceStorage().mostRecentInstance().isPresent()) {
                ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
                if (instance.isUnderAttack() && !instance.hasRebuilt()) {
                    return false;
                }
            }
        }
        return super.damage(damageSource, f);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || !(player instanceof ServerPlayerEntity serverPlayerEntity)) return ActionResult.FAIL;
        TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        if (instance.isUnderAttack()) {
            Set<Item> swords = Set.of(Items.IRON_SWORD, Items.STONE_SWORD, Items.DIAMOND_SWORD, Items.GOLDEN_SWORD, Items.WOODEN_SWORD, Items.NETHERITE_SWORD);
            if (!givenSword && !player.getInventory().containsAny(swords)) { // Use containsAny method as it is present on both server and client
                Runnable giveItem = () -> {
                    MinecraftServer server = player.getServer();
                    if (server != null) {
                        serverPlayerEntity.getInventory().insertStack(new ItemStack(Items.IRON_SWORD));
                        this.givenSword = true;
                    }
                };
                if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) api.executeOnDedicatedServer(giveItem);
                else api.executeOnMain(giveItem);
                return ActionResult.SUCCESS;
            }

            if (instance.getReficuleAttackers().size() == 0) {
                if (!instance.hasRebuilt()) {
                    Runnable fixGuild = () -> {
                        PlayerInventory playerInventory = serverPlayerEntity.getInventory();
                        ItemStack stack = null;
                        for (ItemStack itemStack : playerInventory.main) {
                            if (itemStack.isIn(ItemTags.LOGS)) {
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
                            serverPlayerEntity.getWorld().getEntityById(this.getId()).kill();
                            Translations.GUILDMASTER_THANK_YOU.send(player);
                        } else {
                            Translations.GUILDMASTER_REBUILD.send(player);
                        }
                    };
                    if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) api.executeOnDedicatedServer(fixGuild);
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
    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putBoolean("givenSword", givenSword);
        return super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        this.givenSword = tag.getBoolean("givenSword");
        super.readNbt(tag);
    }
}
