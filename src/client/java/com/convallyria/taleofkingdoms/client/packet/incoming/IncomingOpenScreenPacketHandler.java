package com.convallyria.taleofkingdoms.client.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.BankerScreen;
import com.convallyria.taleofkingdoms.client.gui.entity.BlacksmithScreen;
import com.convallyria.taleofkingdoms.client.gui.entity.GuildMasterScreen;
import com.convallyria.taleofkingdoms.client.gui.entity.InnkeeperScreen;
import com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.CityBuilderBeginGui;
import com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.CityBuilderTierGui;
import com.convallyria.taleofkingdoms.client.gui.entity.kingdom.BlockShopScreen;
import com.convallyria.taleofkingdoms.client.gui.entity.kingdom.ForemanScreen;
import com.convallyria.taleofkingdoms.client.gui.entity.kingdom.ItemShopScreen;
import com.convallyria.taleofkingdoms.client.gui.entity.kingdom.StockMarketScreen;
import com.convallyria.taleofkingdoms.client.gui.entity.kingdom.WardenScreen;
import com.convallyria.taleofkingdoms.client.gui.entity.shop.FoodShopScreen;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.FoodShopEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.BlockShopEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ForemanEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.ItemShopEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.StockMarketEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.warden.WardenEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.packet.s2c.OpenScreenPacket;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

import java.util.Optional;

public final class IncomingOpenScreenPacketHandler extends InClientPacketHandler<OpenScreenPacket> {

    public IncomingOpenScreenPacketHandler() {
        super(Packets.OPEN_CLIENT_SCREEN, OpenScreenPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, OpenScreenPacket openScreen) {
        final OpenScreenPacket.ScreenTypes type = openScreen.type();
        final int entityId = openScreen.entityId();
        context.taskQueue().execute(() -> {
            MinecraftClient client = (MinecraftClient) context.taskQueue();
            if (TaleOfKingdoms.CONFIG.mainConfig.developerMode) {
                final String text = "Received open screen, " + type;
                TaleOfKingdoms.LOGGER.info(text);
                if (client.player != null) client.player.sendMessage(Text.literal(text));
            }

            if (client.player == null) return;

            final Entity entityById = client.world.getEntityById(entityId);
            if (entityById == null) {
                TaleOfKingdoms.LOGGER.warn("Desync! {} was not found.", entityId);
                return;
            }

            final Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance();
            if (instance.isEmpty()) {
                TaleOfKingdoms.LOGGER.warn("Desync! Conquest instance was not found.");
                return;
            }

            Screen screen = switch (type) {
                case GUILD_MASTER -> new GuildMasterScreen(client.player, (GuildMasterEntity) entityById, instance.get());
                case BLACKSMITH -> new BlacksmithScreen(client.player, (BlacksmithEntity) entityById, instance.get());
                case INNKEEPER -> new InnkeeperScreen(client.player, (InnkeeperEntity) entityById, instance.get());
                case BANKER -> new BankerScreen(client.player, (BankerEntity) entityById, instance.get());
                case FOOD_SHOP -> new FoodShopScreen(client.player, (FoodShopEntity) entityById, instance.get());
                case ITEM_SHOP -> new ItemShopScreen(client.player, (ItemShopEntity) entityById, instance.get());
                case BLOCK_SHOP -> new BlockShopScreen(client.player, (BlockShopEntity) entityById, instance.get());
                case CITY_BUILDER_BEGIN -> new CityBuilderBeginGui(client.player, (CityBuilderEntity) entityById, instance.get());
                case CITY_BUILDER_TIER -> new CityBuilderTierGui(client.player, (CityBuilderEntity) entityById, instance.get());
                case STOCK_MARKET -> new StockMarketScreen(client.player, (StockMarketEntity) entityById, instance.get());
                case FOREMAN -> new ForemanScreen(client.player, (ForemanEntity) entityById, instance.get());
                case WARDEN -> new WardenScreen(client.player, (WardenEntity) entityById, instance.get());
            };

            client.setScreen(screen);
        });
    }
}
