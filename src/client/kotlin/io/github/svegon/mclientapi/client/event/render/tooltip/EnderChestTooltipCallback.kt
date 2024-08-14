package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.EnderChestBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

fun interface EnderChestTooltipCallback {
    fun appendEnderChestTooltip(block: EnderChestBlock, stack: ItemStack, context: Item.TooltipContext,
                                tooltip: MutableList<Text>, options: TooltipType
    )

    companion object {
        @JvmField
        val EVENT: Event<EnderChestTooltipCallback> = EventFactory.createArrayBacked(
            EnderChestTooltipCallback::class.java,
            EnderChestTooltipCallback { block, stack, context, tooltip, options -> }
        ) { listeners: Array<EnderChestTooltipCallback> ->
            EnderChestTooltipCallback { block, stack, context, tooltip, options ->
                for (listener in listeners) {
                    listener.appendEnderChestTooltip(block, stack, context, tooltip, options)
                }
            }
        }
    }
}
