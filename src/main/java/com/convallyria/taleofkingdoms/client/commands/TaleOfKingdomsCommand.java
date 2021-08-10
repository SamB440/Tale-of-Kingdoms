package com.convallyria.taleofkingdoms.client.commands;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.entity.Entity;

public class TaleOfKingdomsCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = context.getSource().getEntity();
        if (entity != null) {
            String message = "[\"\",{\"text\":\"Tale of Kingdoms: A new Conquest\",\"bold\":true,\"underlined\":true,\"color\":\"blue\"},{\"text\":\"\\n\"},{\"text\":\"By Cotander/SamB440 & others. (hover)\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://gitlab.com/SamB440/tale-of-kingdoms/-/blob/master/src/main/resources/fabric.mod.json\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Marackai, Aksel0206, PyroPyxel, Sheepguard, michaelb229, The_KingCobra200, Krol05, BeingAmazing(Ben)#6423. Click to view full list.\"}]}},{\"text\":\"\\n\"},{\"text\":\" Take a look at our website: \",\"color\":\"gold\"},{\"text\":\"https://www.convallyria.com\",\"underlined\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.convallyria.com\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Click to view our website.\"}]}},{\"text\":\"\\n\"},{\"text\":\" Join our Discord: \",\"color\":\"gold\"},{\"text\":\"https://discord.gg/fh62mxU\",\"underlined\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/fh62mxU\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Click to join our Discord.\"}]}},{\"text\":\"\\n\"},{\"text\":\"\\n\"},{\"text\":\"Copyright (C) Convallyria 2021\",\"color\":\"gray\"}]";
            entity.sendMessage(ComponentUtils.updateForEntity(context.getSource(), TaleOfKingdoms.parse(new StringReader(message)), entity, 0), Util.NIL_UUID);
            return 1;
        }
        return 0;
    }
}
