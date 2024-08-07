package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import net.minecraft.world.BlockView

fun interface BarrelTooltipCallback {
    fun appendBarrelTooltip(
        block: BarrelBlock?, stack: ItemStack?, world: BlockView?,
        tooltip: List<Text?>?, options: TooltipContext?
    )

    companion object {
        @JvmField
        val EVENT: Event<BarrelTooltipCallback> = EventFactory.createArrayBacked<BarrelTooltipCallback>(
            BarrelTooltipCallback::class.java,
            BarrelTooltipCallback { block: BarrelBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext? -> }) { listeners: Array<BarrelTooltipCallback> ->
            BarrelTooltipCallback { block: BarrelBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, context: TooltipContext? ->
                for (listener in listeners) {
                    listener.appendBarrelTooltip(block, stack, world, tooltip, context)
                }
            }
        }
    }
}
