package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.util.math.MatrixStack
import java.util.function.Function

interface FireOverlayRenderListener {
    fun onFireOverlayRender(matrices: MatrixStack?, callback: CallbackInfo?)

    companion object {
        @JvmField
        val EVENT: Event<FireOverlayRenderListener?> = EventFactory.createArrayBacked<FireOverlayRenderListener?>(
            FireOverlayRenderListener::class.java,
            FireOverlayRenderListener { matrices: MatrixStack?, callback: CallbackInfo? -> },
            Function<Array<FireOverlayRenderListener?>, FireOverlayRenderListener?> { listeners: Array<FireOverlayRenderListener?>? ->
                FireOverlayRenderListener { matrices: MatrixStack?, callback: CallbackInfo? ->
                    for (listener in listeners) {
                        listener.onFireOverlayRender(matrices, callback)

                        if (callback.isCancelled()) {
                            return@FireOverlayRenderListener
                        }
                    }
                }
            })
    }
}
