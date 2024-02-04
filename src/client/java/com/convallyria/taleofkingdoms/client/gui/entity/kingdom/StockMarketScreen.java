package com.convallyria.taleofkingdoms.client.gui.entity.kingdom;

import com.convallyria.taleofkingdoms.client.gui.entity.shop.DefaultShopScreen;
import com.convallyria.taleofkingdoms.client.gui.entity.shop.widget.ShopButtonWidget;
import com.convallyria.taleofkingdoms.client.gui.generic.bar.BarWidget;
import com.convallyria.taleofkingdoms.client.gui.shop.ShopPage;
import com.convallyria.taleofkingdoms.common.entity.kingdom.StockMarketEntity;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Positioning;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockMarketScreen extends DefaultShopScreen {

    private final Map<Integer, List<BarWidget>> barWidgets = new HashMap<>();

    private FlowLayout inner;

    public StockMarketScreen(PlayerEntity player, StockMarketEntity entity, ConquestInstance instance) {
        super(player, entity, instance);
    }

    @Override
    protected void buildShopPages(FlowLayout inner) {
        super.buildShopPages(inner);
        this.inner = inner;
        getShop().getPages().forEach(this::generatePage);
        changePage(0);
    }

    private void generatePage(int pageNumber, ShopPage page) {
        List<BarWidget> widgets = new ArrayList<>();
        for (ShopButtonWidget widget : page.getItems()) {
            final ShopItem shopItem = widget.getShopItem();
            float balancedCost = Math.min(1, (float) (0.3f * shopItem.getModifier()));
            final BarWidget component = (BarWidget) new BarWidget(100, 10, balancedCost)
                    .positioning(Positioning.relative(24 + 35, widget.getY()))
                    .tooltip(List.of(
                            Text.translatable("menu.taleofkingdoms.stock_market.stock_value"),
                            Text.literal("x" + String.format("%.2f", shopItem.getModifier()))
                    ));
            component.visible = component.active = pageNumber == 0;
            inner.child(component);
            widgets.add(component);
        }
        barWidgets.put(pageNumber, widgets);
    }

    @Override
    protected void changePage(int newPage) {
        barWidgets.forEach((pageNumber, widgets) -> {
            for (BarWidget widget : widgets) {
                widget.visible = widget.active = pageNumber == newPage;
            }
        });
    }

    @Override
    protected int getMaxPerSide() {
        return 9;
    }
}
