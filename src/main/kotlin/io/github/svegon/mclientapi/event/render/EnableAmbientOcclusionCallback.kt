package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.EnableAmbientOcclusionCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface EnableAmbientOcclusionCallback {
    fun isAmbientOcclusionEnabled(callback: CallbackInfoReturnable<Boolean?>?)

    companion object {
        @JvmField
        val EVENT: Event<EnableAmbientOcclusionCallback> = EventFactory.createArrayBacked(
            EnableAmbientOcclusionCallback::class.java,
            EnableAmbientOcclusionCallback { callback: CallbackInfoReturnable<Boolean?>? -> }) { listeners: Array<EnableAmbientOcclusionCallback> ->
            EnableAmbientOcclusionCallback { callback: CallbackInfoReturnable<Boolean?> ->
                for (listener in listeners) {
                    listener.isAmbientOcclusionEnabled(callback)

                    if (callback.isCancelled) {
                        return@EnableAmbientOcclusionCallback
                    }
                }
            }
        }
    }
}
