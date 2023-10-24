package com.github.svegon.capi.mixininterface;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public interface IHandledScreen<T extends ScreenHandler> {
    Slot getLastClickedSlot();

    void setLastClickedSlot(Slot lastClickedSlot);

    Slot getTouchHoveredSlot();

    void setTouchHoveredSlot(Slot touchHoveredSlot);

    Slot getTouchDropOriginSlot();

    void setTouchDropOriginSlot(Slot touchDropOriginSlot);

    Slot getTouchDragSlotStart();

    void setTouchDragSlotStart(Slot touchDragSlotStart);

    void renderBackground(DrawContext context, float tickDelta, int mouseX, int mouseY);

    void renderSlot(DrawContext context, Slot slot);

    @Nullable
    Slot slotAt(double x, double y);

    void mouseClicked(int button);

    boolean isOutsideBounds(double mouseX, double mouseY, int left, int top, int button);

    boolean isWithinBounds(int x, int y, int width, int height, double pointX, double pointY);
}
