package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.DispenserBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

fun interface DispenserOrDropperTooltipCallback {
    fun appendDispenserOrDropperTooltip(
        block: DispenserBlock, stack: ItemStack, context: Item.TooltipContext, tooltip: List<Text>, options: TooltipType
    )

    companion object {
        @JvmField
        val EVENT: Event<DispenserOrDropperTooltipCallback> =
            EventFactory.createArrayBacked(
                DispenserOrDropperTooltipCallback::class.java,
                DispenserOrDropperTooltipCallback { block, stack, context, tooltip, options -> }
            ) { listeners: Array<DispenserOrDropperTooltipCallback> ->
                DispenserOrDropperTooltipCallback { block, stack, context, tooltip, options ->
                    for (listener in listeners) {
                        listener.appendDispenserOrDropperTooltip(block, stack, context, tooltip, options)
                    }
                }
            }
    }
}
