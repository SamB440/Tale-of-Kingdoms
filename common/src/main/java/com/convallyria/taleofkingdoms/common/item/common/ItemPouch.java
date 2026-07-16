package com.convallyria.taleofkingdoms.common.item.common;

import java.util.UUID;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemPouch extends Item {

    private final int maxCoins = 1000;

    public ItemPouch(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        final UUID uuid = user.getUUID();
        if (true || world.isClientSide()) return InteractionResultHolder.fail(user.getItemInHand(hand));
//        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
//        final GuildPlayer guildPlayer = instance.getPlayer(user);
        ItemStack itemStack = user.getItemInHand(hand);
//        if (itemStack.hasNbt()) {
//            NbtCompound compoundTag = itemStack.getNbt();
//            if (compoundTag.contains("coins")) {
//                int coins = compoundTag.getInt("coins");
//                if (coins == maxCoins) {
//                    guildPlayer.setCoins(guildPlayer.getCoins() + coins);
//                    compoundTag.remove("coins");
//                    user.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER, 1f, 1f);
//                    itemStack.setNbt(compoundTag);
//                    return TypedActionResult.pass(itemStack);
//                }
//            }
//        }
//
//        if (guildPlayer.getCoins() >= 100) {
//            if (!itemStack.hasNbt()) {
//                NbtCompound compoundTag = new NbtCompound();
//                compoundTag.putInt("coins", 100);
//                guildPlayer.setCoins(guildPlayer.getCoins() - 100);
//                itemStack.setNbt(compoundTag);
//                user.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1f, 1f);
//            } else {
//                NbtCompound compoundTag = itemStack.getNbt();
//                if (!compoundTag.contains("coins")) {
//                    compoundTag.putInt("coins", 100);
//                    guildPlayer.setCoins(guildPlayer.getCoins() - 100);
//                    user.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1f, 1f);
//                } else {
//                    int coins = compoundTag.getInt("coins");
//                    if (coins <= (maxCoins - 100)) {
//                        compoundTag.putInt("coins", coins + 100);
//                        guildPlayer.setCoins(guildPlayer.getCoins() - 100);
//                        user.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1f, 1f);
//                    }
//                }
//                itemStack.setNbt(compoundTag);
//            }
//        }
        return InteractionResultHolder.pass(itemStack);
    }

//    @Override
//    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
//        if (stack.hasNbt()) {
//            NbtCompound compoundTag = stack.getNbt();
//            if (compoundTag.contains("coins")) {
//                int coins = compoundTag.getInt("coins");
//                tooltip.add(Text.literal("Coins: " + coins).formatted(Formatting.GOLD, Formatting.ITALIC));
//            }
//        }
//    }
}
