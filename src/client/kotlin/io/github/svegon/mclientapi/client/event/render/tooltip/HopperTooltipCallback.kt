package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.HopperBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

fun interface HopperTooltipCallback {
    fun appendHopperTooltip(
        block: HopperBlock, stack: ItemStack, context: Item.TooltipContext,
        tooltip: MutableList<Text>, options: TooltipType
    )

    companion object {
        @JvmField
        val EVENT: Event<HopperTooltipCallback> = EventFactory.createArrayBacked(
            HopperTooltipCallback::class.java,
            HopperTooltipCallback { block: HopperBlock, stack: ItemStack, context: Item.TooltipContext,
                                    tooltip: List<Text>, options: TooltipType -> }) { listeners: Array<HopperTooltipCallback> ->
            HopperTooltipCallback { block: HopperBlock, stack: ItemStack, context: Item.TooltipContext,
                                    tooltip, options: TooltipType ->
                for (listener in listeners) {
                    listener.appendHopperTooltip(block, stack, context, tooltip, options)
                }
            }
        }
    }
}
