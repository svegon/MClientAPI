package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import net.minecraft.world.BlockView
import java.util.function.Function

interface ShulkerBoxTooltipCallback {
    fun appendShulkerBoxTooltip(
        stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?,
        options: TooltipContext?, callback: CallbackInfo?
    )

    companion object {
        @JvmField
        val EVENT: Event<ShulkerBoxTooltipCallback?> = EventFactory.createArrayBacked<ShulkerBoxTooltipCallback?>(
            ShulkerBoxTooltipCallback::class.java,
            ShulkerBoxTooltipCallback { stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext?, callback: CallbackInfo? -> },
            Function<Array<ShulkerBoxTooltipCallback?>, ShulkerBoxTooltipCallback?> { listeners: Array<ShulkerBoxTooltipCallback?>? ->
                ShulkerBoxTooltipCallback { stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, context: TooltipContext?, callback: CallbackInfo? ->
                    for (listener in listeners) {
                        listener.appendShulkerBoxTooltip(stack, world, tooltip, context, callback)

                        if (callback.isCancelled()) {
                            return@ShulkerBoxTooltipCallback
                        }
                    }
                }
            })
    }
}
