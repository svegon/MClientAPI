package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.gui.DrawContext
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot

interface IHandledScreen<T : ScreenHandler> {
    var lastClickedSlot: Slot?

    var touchHoveredSlot: Slot?

    var touchDropOriginSlot: Slot?

    var touchDragSlotStart: Slot?

    fun renderBackground(context: DrawContext, tickDelta: Float, mouseX: Int, mouseY: Int)

    fun renderSlot(context: DrawContext, slot: Slot)

    fun slotAt(x: Double, y: Double): Slot?

    fun mouseClicked(button: Int)

    fun isOutsideBounds(mouseX: Double, mouseY: Double, left: Int, top: Int, button: Int): Boolean

    fun isWithinBounds(x: Int, y: Int, width: Int, height: Int, pointX: Double, pointY: Double): Boolean
}
