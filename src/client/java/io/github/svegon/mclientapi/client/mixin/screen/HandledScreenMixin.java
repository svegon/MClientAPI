package io.github.svegon.mclientapi.client.mixin.screen;

import io.github.svegon.mclientapi.client.mixinterface.IHandledScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractContainerScreen.class)
public abstract class HandledScreenMixin<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T>,
        IHandledScreen<T> {
    @Shadow protected @Nullable Slot hoveredSlot;
    @Shadow private @Nullable Slot clickedSlot;
    @Shadow private @Nullable Slot quickdropSlot;
    @Shadow private @Nullable Slot lastClickSlot;
    @Shadow private @Nullable Slot getHoveredSlot(final double x, final double y) { throw new AssertionError(); }
    @Shadow protected abstract boolean hasClickedOutside(final double mx, final double my, final int xo, final int yo);

    @Shadow
    protected abstract void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY);

    @Override
    public @Nullable Slot getMClientAPI$hoveredSlot() {
        return hoveredSlot;
    }

    @Override
    public void setMClientAPI$hoveredSlot(@Nullable Slot slot) {
        hoveredSlot = slot;
    }

    @Override
    public @Nullable Slot getMClientAPI$clickedSlot() {
        return clickedSlot;
    }

    @Override
    public void setMClientAPI$clickedSlot(@Nullable Slot slot) {
        clickedSlot = slot;
    }

    @Override
    public @Nullable Slot getMClientAPI$lastClickSlot() {
        return lastClickSlot;
    }

    @Override
    public void setMClientAPI$lastClickSlot(@Nullable Slot slot) {
        lastClickSlot = slot;
    }

    @Override
    public @Nullable Slot getMClientAPI$quickdropSlot() {
        return quickdropSlot;
    }

    @Override
    public void setMClientAPI$quickdropSlot(@Nullable Slot slot) {
        quickdropSlot = slot;
    }

    @Override
    public void mClientAPI$renderSlot(@NotNull GuiGraphicsExtractor graphics, @NotNull Slot slot, int mouseX, int mouseY) {
        extractSlot(graphics, slot, mouseX, mouseY);
    }

    @Override
    public @Nullable Slot mClientAPI$slotAt(double x, double y) {
        return getHoveredSlot(x, y);
    }

    @Override
    public boolean mClientAPI$sOutsideScreen(double mouseX, double mouseY, int left, int top) {
        return hasClickedOutside(mouseX, mouseY, left, top);
    }

    private HandledScreenMixin(Component title) {
        super(title);
    }

}
