package com.convallyria.taleofkingdoms.common.item.common;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemPouch extends Item {

    private final int maxCoins = 1000;

    public ItemPouch(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide()) return InteractionResultHolder.fail(user.getItemInHand(hand));
        ClientConquestInstance instance = (ClientConquestInstance) TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        ItemStack itemStack = user.getItemInHand(hand);
        if (itemStack.hasTag()) {
            CompoundTag compoundTag = itemStack.getTag();
            if (compoundTag.contains("coins")) {
                int coins = compoundTag.getInt("coins");
                if (coins == maxCoins) {
                    instance.setCoins(instance.getCoins() + coins);
                    compoundTag.remove("coins");
                    user.playNotifySound(SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.MASTER, 1f, 1f);
                    itemStack.setTag(compoundTag);
                    return InteractionResultHolder.pass(itemStack);
                }
            }
        }

        if (instance.getCoins() >= 100) {
            if (!itemStack.hasTag()) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putInt("coins", 100);
                instance.setCoins(instance.getCoins() - 100);
                itemStack.setTag(compoundTag);
                user.playNotifySound(SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.MASTER, 1f, 1f);
            } else {
                CompoundTag compoundTag = itemStack.getTag();
                if (!compoundTag.contains("coins")) {
                    compoundTag.putInt("coins", 100);
                    instance.setCoins(instance.getCoins() - 100);
                    user.playNotifySound(SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.MASTER, 1f, 1f);
                } else {
                    int coins = compoundTag.getInt("coins");
                    if (coins <= (maxCoins - 100)) {
                        compoundTag.putInt("coins", coins + 100);
                        instance.setCoins(instance.getCoins() - 100);
                        user.playNotifySound(SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.MASTER, 1f, 1f);
                    }
                }
                itemStack.setTag(compoundTag);
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        if (stack.hasTag()) {
            CompoundTag compoundTag = stack.getTag();
            if (compoundTag.contains("coins")) {
                int coins = compoundTag.getInt("coins");
                tooltip.add(new TextComponent("Coins: " + coins).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
            }
        }
    }
}
