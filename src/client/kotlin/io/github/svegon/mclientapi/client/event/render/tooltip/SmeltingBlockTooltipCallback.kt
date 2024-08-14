package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.AbstractFurnaceBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

fun interface SmeltingBlockTooltipCallback {
    fun appendSmeltingBlockTooltip(
        block: AbstractFurnaceBlock, stack: ItemStack, context: Item.TooltipContext,
        tooltip: MutableList<Text>, options: TooltipType
    )

    companion object {
        @JvmField
        val EVENT: Event<SmeltingBlockTooltipCallback> = EventFactory.createArrayBacked<SmeltingBlockTooltipCallback>(
            SmeltingBlockTooltipCallback::class.java,
            SmeltingBlockTooltipCallback { block: AbstractFurnaceBlock, stack: ItemStack, context: Item.TooltipContext,
                                           tooltip, options: TooltipType -> })
        { listeners: Array<SmeltingBlockTooltipCallback> -> SmeltingBlockTooltipCallback {
            block: AbstractFurnaceBlock, stack: ItemStack, context: Item.TooltipContext,
            tooltip, options: TooltipType ->
                for (listener in listeners) {
                    listener.appendSmeltingBlockTooltip(block, stack, context, tooltip, options)
                }
            }
        }
    }
}
