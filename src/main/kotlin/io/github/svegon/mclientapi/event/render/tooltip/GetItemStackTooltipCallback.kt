package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text

fun interface GetItemStackTooltipCallback {
    fun getItemStackTooltip(
        stack: ItemStack?, player: PlayerEntity?, context: TooltipContext?,
        callback: CallbackInfoReturnable<List<Text?>?>?
    )

    companion object {
        @JvmField
        val EVENT: Event<GetItemStackTooltipCallback> = EventFactory.createArrayBacked<GetItemStackTooltipCallback>(
            GetItemStackTooltipCallback::class.java,
            GetItemStackTooltipCallback { stack: ItemStack?, player: PlayerEntity?, context: TooltipContext?, callback: CallbackInfoReturnable<List<Text?>?>? -> }) { listeners: Array<GetItemStackTooltipCallback> ->
            GetItemStackTooltipCallback { stack: ItemStack?, player: PlayerEntity?, context: TooltipContext?, callback: CallbackInfoReturnable<List<Text?>?> ->
                for (listener in listeners) {
                    listener.getItemStackTooltip(stack, player, context, callback)

                    if (callback.isCancelled()) {
                        return@GetItemStackTooltipCallback
                    }
                }
            }
        }
    }
}
