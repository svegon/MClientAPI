package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import net.minecraft.world.BlockView

fun interface HopperTooltipCallback {
    fun appendHopperTooltip(
        block: HopperBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?,
        options: TooltipContext?
    )

    companion object {
        @JvmField
        val EVENT: Event<HopperTooltipCallback> = EventFactory.createArrayBacked<HopperTooltipCallback>(
            HopperTooltipCallback::class.java,
            HopperTooltipCallback { block: HopperBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext? -> }) { listeners: Array<HopperTooltipCallback> ->
            HopperTooltipCallback { block: HopperBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, context: TooltipContext? ->
                for (listener in listeners) {
                    listener.appendHopperTooltip(block, stack, world, tooltip, context)
                }
            }
        }
    }
}
