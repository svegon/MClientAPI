package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot

interface IHandledScreen<T : AbstractContainerMenu> {
    var `mClientAPI$hoveredSlot`: Slot?
    var `mClientAPI$clickedSlot`: Slot?
    var `mClientAPI$quickdropSlot`: Slot?
    var `mClientAPI$lastClickSlot`: Slot?

    fun `mClientAPI$renderSlot`(graphics: GuiGraphicsExtractor, slot: Slot, mouseX: Int, mouseY: Int)

    fun `mClientAPI$slotAt`(x: Double, y: Double): Slot?

    fun `mClientAPI$sOutsideScreen`(mouseX: Double, mouseY: Double, left: Int, top: Int): Boolean
}
