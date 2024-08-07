package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import net.minecraft.world.BlockView

fun interface SmeltingBlockTooltipCallback {
    fun appendSmeltingBlockTooltip(
        block: AbstractFurnaceBlock?, stack: ItemStack?, world: BlockView?,
        tooltip: List<Text?>?, options: TooltipContext?
    )

    companion object {
        @JvmField
        val EVENT: Event<SmeltingBlockTooltipCallback> = EventFactory.createArrayBacked<SmeltingBlockTooltipCallback>(
            SmeltingBlockTooltipCallback::class.java,
            SmeltingBlockTooltipCallback { block: AbstractFurnaceBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext? -> }) { listeners: Array<SmeltingBlockTooltipCallback> ->
            SmeltingBlockTooltipCallback { block: AbstractFurnaceBlock?, stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, context: TooltipContext? ->
                for (listener in listeners) {
                    listener.appendSmeltingBlockTooltip(block, stack, world, tooltip, context)
                }
            }
        }
    }
}
