package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import net.minecraft.world.BlockView

fun interface CommandBlockTooltipCallback {
    fun appendCommandBlockTooltip(
        block: CommandBlock?, stack: ItemStack?, world: BlockView?,
        tooltip: List<Text?>?, options: TooltipContext?
    )

    companion object {
        @JvmField
        val EVENT: Event<CommandBlockTooltipCallback> = EventFactory.createArrayBacked<CommandBlockTooltipCallback>(
            CommandBlockTooltipCallback::class.java,
            CommandBlockTooltipCallback { block: CommandBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext? -> }) { listeners: Array<CommandBlockTooltipCallback> ->
            CommandBlockTooltipCallback { block: CommandBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, context: TooltipContext? ->
                for (listener in listeners) {
                    listener.appendCommandBlockTooltip(block, stack, world, tooltip, context)
                }
            }
        }
    }
}
