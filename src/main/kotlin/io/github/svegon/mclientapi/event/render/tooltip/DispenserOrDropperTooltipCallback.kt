package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import net.minecraft.world.BlockView

fun interface DispenserOrDropperTooltipCallback {
    fun appendDispenserOrDropperTooltip(
        block: DispenserBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?,
        options: TooltipContext?
    )

    companion object {
        @JvmField
        val EVENT: Event<DispenserOrDropperTooltipCallback> =
            EventFactory.createArrayBacked<DispenserOrDropperTooltipCallback>(
                DispenserOrDropperTooltipCallback::class.java,
                DispenserOrDropperTooltipCallback { block: DispenserBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext? -> }) { listeners: Array<DispenserOrDropperTooltipCallback> ->
                DispenserOrDropperTooltipCallback { block: DispenserBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, context: TooltipContext? ->
                    for (listener in listeners) {
                        listener.appendDispenserOrDropperTooltip(block, stack, world, tooltip, context)
                    }
                }
            }
    }
}
