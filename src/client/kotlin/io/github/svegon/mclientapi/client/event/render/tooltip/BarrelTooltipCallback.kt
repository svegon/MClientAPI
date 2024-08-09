package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BarrelBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

fun interface BarrelTooltipCallback {
    fun appendBarrelTooltip(
        block: BarrelBlock, stack: ItemStack, context: Item.TooltipContext, tooltip: List<Text>, options: TooltipType
    )

    companion object {
        @JvmField
        val EVENT: Event<BarrelTooltipCallback> = EventFactory.createArrayBacked(
            BarrelTooltipCallback::class.java,
            BarrelTooltipCallback { block, stack, context, tooltip, options -> }
        ) {
            listeners -> BarrelTooltipCallback { block, stack, context, tooltip, options -> for (listener in listeners)
                listener.appendBarrelTooltip(block, stack, context, tooltip, options) }
        }
    }
}
