package io.github.svegon.capi.mixin;

import io.github.svegon.capi.mixininterface.IHandledScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T>,
        IHandledScreen<T> {
    @Shadow private @Nullable Slot lastClickedSlot;

    @Shadow private @Nullable Slot touchHoveredSlot;

    @Shadow private @Nullable Slot touchDropOriginSlot;

    @Shadow private @Nullable Slot touchDragSlotStart;

    @Shadow protected abstract void drawBackground(DrawContext var1, float var2, int var3, int var4);

    @Shadow protected abstract void drawSlot(DrawContext context, Slot slot);

    @Shadow @Nullable protected abstract Slot getSlotAt(double x, double y);

    @Shadow protected int x;

    @Shadow protected abstract void onMouseClick(int button);

    @Shadow protected abstract boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button);

    @Shadow protected abstract boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY);

    private HandledScreenMixin(Text title) {
        super(title);
    }

    public Slot getLastClickedSlot() {
        return lastClickedSlot;
    }

    public void setLastClickedSlot(Slot lastClickedSlot) {
        this.lastClickedSlot = lastClickedSlot;
    }

    public Slot getTouchHoveredSlot() {
        return touchHoveredSlot;
    }

    public void setTouchHoveredSlot(Slot touchHoveredSlot) {
        this.touchHoveredSlot = touchHoveredSlot;
    }

    public Slot getTouchDropOriginSlot() {
        return touchDropOriginSlot;
    }

    public void setTouchDropOriginSlot(Slot touchDropOriginSlot) {
        this.touchDropOriginSlot = touchDropOriginSlot;
    }

    public Slot getTouchDragSlotStart() {
        return touchDragSlotStart;
    }

    public void setTouchDragSlotStart(Slot touchDragSlotStart) {
        this.touchDragSlotStart = touchDragSlotStart;
    }

    @Override
    public void renderBackground(DrawContext context, float tickDelta, int mouseX, int mouseY) {
        drawBackground(context, tickDelta, mouseX, mouseY);
    }

    @Override
    public void renderSlot(DrawContext context, Slot slot) {
        drawSlot(context, slot);
    }

    @Override
    public @Nullable Slot slotAt(double x, double y) {
        return getSlotAt(x, y);
    }

    @Override
    public void mouseClicked(int button) {
        onMouseClick(button);
    }

    @Override
    public boolean isOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        return isClickOutsideBounds(mouseX, mouseY, left, top, button);
    }

    @Override
    public boolean isWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
        return isPointWithinBounds(x, y, width, height, pointX, pointY);
    }
}
