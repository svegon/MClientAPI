package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface ShulkerBoxTooltipListener {
    fun appendShulkerBoxTooltip(
        block: ShulkerBoxBlock, stack: ItemStack, context: Item.TooltipContext,
        tooltip: MutableList<Text>, options: TooltipType, callback: CallbackInfo
    )

    companion object {
        @JvmField
        val EVENT: Event<ShulkerBoxTooltipListener> = EventFactory.createArrayBacked(
            ShulkerBoxTooltipListener::class.java,
            ShulkerBoxTooltipListener { block: ShulkerBoxBlock, stack: ItemStack, context: Item.TooltipContext,
                                        tooltip: List<Text>, options: TooltipType, callback: CallbackInfo -> }
        ) { listeners: Array<ShulkerBoxTooltipListener> ->
            ShulkerBoxTooltipListener { block: ShulkerBoxBlock, stack: ItemStack, context: Item.TooltipContext,
                                        tooltip, options: TooltipType, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.appendShulkerBoxTooltip(block, stack, context, tooltip, options, callback)

                    if (callback.isCancelled) {
                        return@ShulkerBoxTooltipListener
                    }
                }
            }
        }
    }
}
