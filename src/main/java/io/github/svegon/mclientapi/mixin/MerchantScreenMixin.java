package io.github.svegon.capi.mixin;

import io.github.svegon.capi.mixininterface.IMerchantScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin extends HandledScreen<MerchantScreenHandler> implements IMerchantScreen {
    @Shadow private int selectedIndex;

    @Shadow private boolean scrolling;

    @Shadow
    int indexStartOffset;

    private MerchantScreenMixin(MerchantScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Shadow protected abstract void syncRecipeIndex();

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public boolean isScrolling() {
        return scrolling;
    }

    public void setScrolling(boolean scrolling) {
        this.scrolling = scrolling;
    }

    public int getIndexStartOffset() {
        return indexStartOffset;
    }

    public void setIndexStartOffset(int indexStartOffset) {
        this.indexStartOffset = indexStartOffset;
    }

    @Override
    public void syncSelectedRecipe() {
        syncRecipeIndex();
    }
}
