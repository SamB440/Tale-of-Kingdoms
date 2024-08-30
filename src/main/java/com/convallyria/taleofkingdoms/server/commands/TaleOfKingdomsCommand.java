package com.convallyria.taleofkingdoms.server.commands;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Texts;

public class TaleOfKingdomsCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Entity entity = context.getSource().getEntity();
        if (entity != null) {
            String message = "[\"\",{\"text\":\"Tale of Kingdoms: A new Conquest\",\"bold\":true,\"underlined\":true,\"color\":\"blue\"},{\"text\":\"\\n\"},{\"text\":\"By Cotander/SamB440 & others. (hover)\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://gitlab.com/SamB440/tale-of-kingdoms/-/blob/master/src/main/resources/fabric.mod.json\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Marackai, Aksel0206, PyroPyxel, Sheepguard, michaelb229, The_KingCobra200, Krol05, BeingAmazing(Ben)#6423. Click to view full list.\"}]}},{\"text\":\"\\n\"},{\"text\":\" Take a look at our website: \",\"color\":\"gold\"},{\"text\":\"https://www.convallyria.com\",\"underlined\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.convallyria.com\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Click to view our website.\"}]}},{\"text\":\"\\n\"},{\"text\":\" Join our Discord: \",\"color\":\"gold\"},{\"text\":\"https://discord.gg/fh62mxU\",\"underlined\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/fh62mxU\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"Click to join our Discord.\"}]}},{\"text\":\"\\n\"},{\"text\":\"\\n\"},{\"text\":\"Copyright (C) Convallyria 2021\",\"color\":\"gray\"}]";
            entity.sendMessage(Texts.parse(context.getSource(), TaleOfKingdoms.parse(new StringReader(message), entity.getRegistryManager()), entity, 0));
            return 1;
        }
        return 0;
    }
}
