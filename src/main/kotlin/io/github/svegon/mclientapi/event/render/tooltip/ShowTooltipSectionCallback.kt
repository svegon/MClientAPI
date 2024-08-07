package io.github.svegon.capi.event.render.tooltip

import io.github.svegon.capi.event.render.tooltip.ShowTooltipSectionCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface ShowTooltipSectionCallback {
    fun shouldShowTooltipSection(
        flags: Int, tooltipSection: ItemStack.TooltipSection?,
        callback: CallbackInfoReturnable<Boolean?>?
    )

    companion object {
        @JvmField
        val EVENT: Event<ShowTooltipSectionCallback> = EventFactory.createArrayBacked<ShowTooltipSectionCallback>(
            ShowTooltipSectionCallback::class.java,
            ShowTooltipSectionCallback { flags: Int, tooltipSection: ItemStack.TooltipSection?, callback: CallbackInfoReturnable<Boolean?>? -> }) { listeners: Array<ShowTooltipSectionCallback> ->
            ShowTooltipSectionCallback { flags: Int, tooltipSection: ItemStack.TooltipSection?, callback: CallbackInfoReturnable<Boolean?> ->
                for (listener in listeners) {
                    listener.shouldShowTooltipSection(flags, tooltipSection, callback)

                    if (callback.isCancelled) {
                        return@ShowTooltipSectionCallback
                    }
                }
            }
        }
    }
}
