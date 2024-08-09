package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BrewingStandBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

fun interface BrewingStandTooltipCallback {
    fun appendBrewingStandTooltip(
        block: BrewingStandBlock, stack: ItemStack, context: Item.TooltipContext, tooltip: List<Text>,
        options: TooltipType
    )

    companion object {
        @JvmField
        val EVENT: Event<BrewingStandTooltipCallback> = EventFactory.createArrayBacked<BrewingStandTooltipCallback>(
            BrewingStandTooltipCallback::class.java,
            BrewingStandTooltipCallback { block: BrewingStandBlock, stack: ItemStack, context: Item.TooltipContext,
                                          tooltip: List<Text>, options: TooltipType -> }) {
            listeners: Array<BrewingStandTooltipCallback> ->
            BrewingStandTooltipCallback { block: BrewingStandBlock, stack: ItemStack, context: Item.TooltipContext,
                                          tooltip: List<Text>, options: TooltipType ->
                for (listener in listeners) {
                    listener.appendBrewingStandTooltip(block, stack, context, tooltip, options)
                }
            }
        }
    }
}
