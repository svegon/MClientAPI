package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import net.minecraft.world.BlockView

fun interface EnderChestTooltipCallback {
    fun appendEnderChestTooltip(stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext?)

    companion object {
        @JvmField
        val EVENT: Event<EnderChestTooltipCallback> = EventFactory.createArrayBacked<EnderChestTooltipCallback>(
            EnderChestTooltipCallback::class.java,
            EnderChestTooltipCallback { stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext? -> }) { listeners: Array<EnderChestTooltipCallback> ->
            EnderChestTooltipCallback { stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, context: TooltipContext? ->
                for (listener in listeners) {
                    listener.appendEnderChestTooltip(stack, world, tooltip, context)
                }
            }
        }
    }
}
