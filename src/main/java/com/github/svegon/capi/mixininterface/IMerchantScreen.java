package com.github.svegon.capi.mixininterface;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.village.TradeOffer;

public interface IMerchantScreen {
    int getSelectedIndex();

    void setSelectedIndex(int selectedIndex);

    boolean isScrolling();

    void setScrolling(boolean scrolling);

    int getIndexStartOffset();

    void setIndexStartOffset(int indexStartOffset);

    void syncSelectedRecipe();
}
