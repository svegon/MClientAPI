package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.ChestBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

fun interface ChestTooltipCallback {
    fun appendChestTooltip(
        block: ChestBlock, stack: ItemStack, context: Item.TooltipContext,
        tooltip: MutableList<Text>, options: TooltipType
    )

    companion object {
        @JvmField
        val EVENT: Event<ChestTooltipCallback> = EventFactory.createArrayBacked(
            ChestTooltipCallback::class.java,
            ChestTooltipCallback { block, stack, context, tooltip, options -> }
        ) {
            listeners -> ChestTooltipCallback { block, stack, context, tooltip, options -> for (listener in listeners)
                listener.appendChestTooltip(block, stack, context, tooltip, options) }
        }
    }
}
