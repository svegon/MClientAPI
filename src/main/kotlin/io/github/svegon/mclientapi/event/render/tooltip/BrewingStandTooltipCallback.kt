package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import net.minecraft.world.BlockView

fun interface BrewingStandTooltipCallback {
    fun appendBrewingStandTooltip(
        block: BrewingStandBlock?, stack: ItemStack?, world: BlockView?,
        tooltip: List<Text?>?, options: TooltipContext?
    )

    companion object {
        @JvmField
        val EVENT: Event<BrewingStandTooltipCallback> = EventFactory.createArrayBacked<BrewingStandTooltipCallback>(
            BrewingStandTooltipCallback::class.java,
            BrewingStandTooltipCallback { block: BrewingStandBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext? -> }) { listeners: Array<BrewingStandTooltipCallback> ->
            BrewingStandTooltipCallback { block: BrewingStandBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, context: TooltipContext? ->
                for (listener in listeners) {
                    listener.appendBrewingStandTooltip(block, stack, world, tooltip, context)
                }
            }
        }
    }
}
