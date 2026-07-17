package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.advancement.TOKAdvancements;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.FollowPlayerGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.HealPlayerGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServerAPI;
import net.fabricmc.api.EnvType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import java.util.Optional;
import java.util.Set;

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
        this.targetSelector.addGoal(1, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_SOLDIER.get(), false));
        this.targetSelector.addGoal(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_GUARDIAN.get(), false));
        this.targetSelector.addGoal(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_MAGE.get(), false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 100,
                true, true, livingEntity -> livingEntity instanceof Enemy));
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    public boolean fireImmune() {
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
    public boolean hurt(DamageSource damageSource, float f) {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api != null) {
            if (api.getConquestInstanceStorage().mostRecentInstance().isPresent()) {
                ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
                if (instance.isUnderAttack()) {
                    return false;
                }
            }
        }
        return super.hurt(damageSource, f);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND || !(player instanceof ServerPlayer serverPlayerEntity)) return InteractionResult.FAIL;
        TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        if (instance.isUnderAttack()) {
            Set<Item> swords = Set.of(Items.IRON_SWORD, Items.STONE_SWORD, Items.DIAMOND_SWORD, Items.GOLDEN_SWORD, Items.WOODEN_SWORD, Items.NETHERITE_SWORD);
            if (!givenSword && !player.getInventory().hasAnyOf(swords)) { // Use containsAny method as it is present on both server and client
                Runnable giveItem = () -> {
                    MinecraftServer server = player.getServer();
                    if (server != null) {
                        serverPlayerEntity.getInventory().add(new ItemStack(Items.IRON_SWORD));
                        this.givenSword = true;
                    }
                };
                api.executeOnMain(giveItem);
                return InteractionResult.SUCCESS;
            }

            if (instance.getReficuleAttackers().isEmpty()) {
                final GuildPlayer guildPlayer = instance.getPlayer(player);
                if (!guildPlayer.hasRebuiltGuild() && guildPlayer.getWorthiness() >= 750 && guildPlayer.getKingdom() == null) {
                    Runnable fixGuild = () -> {
                        Inventory playerInventory = serverPlayerEntity.getInventory();
                        ItemStack stack = null;
                        for (ItemStack itemStack : playerInventory.items) {
                            if (itemStack.is(ItemTags.LOGS)) {
                                if (itemStack.getCount() == 64) {
                                    stack = itemStack;
                                    break;
                                }
                            }
                        }

                        if (stack != null) {
                            playerInventory.setItem(InventoryUtils.getSlotWithStack(playerInventory, stack), new ItemStack(Items.AIR));
                            instance.rebuild(serverPlayerEntity, api);
                            guildPlayer.setHasRebuiltGuild(true);
                            TOKAdvancements.award(serverPlayerEntity, TOKAdvancements.REBUILD_GUILD);
                            instance.setUnderAttack(false);
                            final Entity entity = serverPlayerEntity.level().getEntity(this.getId());
                            entity.teleportTo(entity.getX(), entity.getY() + 100, entity.getZ());
                            entity.remove(RemovalReason.DISCARDED);
                            Translations.GUILDMASTER_THANK_YOU.send(player);
                        } else {
                            Translations.GUILDMASTER_REBUILD.send(player);
                        }
                    };
                    if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) ((TaleOfKingdomsServerAPI) api).executeOnDedicatedServer(fixGuild);
                    else if (serverPlayerEntity.getServer() == null || !serverPlayerEntity.getServer().isDedicatedServer()) api.executeOnMain(fixGuild);
                    return InteractionResult.SUCCESS;
                }
            } else if (instance.getReficuleAttackers().size() <= 4) {
                Translations.GUILDMASTER_KILL_REFICULES.send(player);
            } else {
                Translations.GUILDMASTER_STAY_CLOSE.send(player);
            }
        }
        return InteractionResult.SUCCESS;
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
