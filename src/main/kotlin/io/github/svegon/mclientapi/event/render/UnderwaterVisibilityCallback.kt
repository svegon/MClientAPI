package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.network.ClientPlayerEntity

fun interface UnderwaterVisibilityCallback {
    fun onUnderwaterVisibility(player: ClientPlayerEntity?, callback: CallbackInfoReturnable<Float?>?)

    companion object {
        @JvmField
        val EVENT: Event<UnderwaterVisibilityCallback> = EventFactory.createArrayBacked<UnderwaterVisibilityCallback>(
            UnderwaterVisibilityCallback::class.java,
            UnderwaterVisibilityCallback { player: ClientPlayerEntity?, callback: CallbackInfoReturnable<Float?>? -> }) { listeners: Array<UnderwaterVisibilityCallback> ->
            UnderwaterVisibilityCallback { player: ClientPlayerEntity?, callback: CallbackInfoReturnable<Float?> ->
                for (listener in listeners) {
                    listener.onUnderwaterVisibility(player, callback)

                    if (callback.isCancelled()) {
                        return@UnderwaterVisibilityCallback
                    }
                }
            }
        }
    }
}
