package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.CommandBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

fun interface CommandBlockTooltipCallback {
    fun appendCommandBlockTooltip(
        block: CommandBlock, stack: ItemStack, context: Item.TooltipContext,
        tooltip: MutableList<Text>, options: TooltipType
    )

    companion object {
        @JvmField
        val EVENT: Event<CommandBlockTooltipCallback> = EventFactory.createArrayBacked(
            CommandBlockTooltipCallback::class.java,
            CommandBlockTooltipCallback { block, stack, context, tooltip, options -> }
        ) { listeners ->
            CommandBlockTooltipCallback { block, stack, context, tooltip, options ->
                for (listener in listeners) {
                    listener.appendCommandBlockTooltip(block, stack, context, tooltip, options)
                }
            }
        }
    }
}
