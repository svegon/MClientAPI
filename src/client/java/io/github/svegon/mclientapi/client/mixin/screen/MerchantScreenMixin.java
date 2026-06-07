package io.github.svegon.mclientapi.client.mixin.screen;

import io.github.svegon.mclientapi.client.mixinterface.IMerchantScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MerchantMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin extends AbstractContainerScreen<MerchantMenu> implements IMerchantScreen {
    @Shadow private int shopItem;
    @Shadow private int scrollOff;
    @Shadow private boolean isDragging;;

    @Override
    public int getMClientAPI$selectedOffer() {
        return shopItem;
    }

    @Override
    public void setMClientAPI$selectedOffer(int i) {
        shopItem = i;
    }

    public boolean isScrolling() {
        return isDragging;
    }

    public void setScrolling(boolean scrolling) {
        isDragging = scrolling;
    }

    public int getIndexStartOffset() {
        return shopItem;
    }

    public void setIndexStartOffset(int indexStartOffset) {
        scrollOff = indexStartOffset;
    }

    @Override
    public void syncSelectedRecipe() {
        this.minecraft.getConnection().send(new ServerboundSelectTradePacket(this.shopItem));
    }

    private MerchantScreenMixin(MerchantMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
}
