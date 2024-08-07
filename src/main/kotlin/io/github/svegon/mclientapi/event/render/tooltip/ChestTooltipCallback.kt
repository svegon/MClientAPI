package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import net.minecraft.world.BlockView

fun interface ChestTooltipCallback {
    fun appendChestTooltip(
        block: ChestBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?,
        options: TooltipContext?
    )

    companion object {
        @JvmField
        val EVENT: Event<ChestTooltipCallback> = EventFactory.createArrayBacked<ChestTooltipCallback>(
            ChestTooltipCallback::class.java,
            ChestTooltipCallback { block: ChestBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext? -> }) { listeners: Array<ChestTooltipCallback> ->
            ChestTooltipCallback { block: ChestBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, context: TooltipContext? ->
                for (listener in listeners) {
                    listener.appendChestTooltip(block, stack, world, tooltip, context)
                }
            }
        }
    }
}
