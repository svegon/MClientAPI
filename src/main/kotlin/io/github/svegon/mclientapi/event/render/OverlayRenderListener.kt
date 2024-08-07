package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import java.util.function.Function

interface OverlayRenderListener {
    fun onOverlayRender(context: DrawContext?, texture: Identifier?, opacity: Float, callback: CallbackInfo?)

    companion object {
        @JvmField
        val EVENT: Event<OverlayRenderListener?> = EventFactory.createArrayBacked<OverlayRenderListener?>(
            OverlayRenderListener::class.java,
            OverlayRenderListener { context: DrawContext?, texture: Identifier?, scale: Float, callback: CallbackInfo? -> },
            Function<Array<OverlayRenderListener?>, OverlayRenderListener?> { listeners: Array<OverlayRenderListener?>? ->
                OverlayRenderListener { context: DrawContext?, texture: Identifier?, scale: Float, callback: CallbackInfo? ->
                    for (listener in listeners) {
                        listener.onOverlayRender(context, texture, scale, callback)

                        if (callback.isCancelled()) {
                            return@OverlayRenderListener
                        }
                    }
                }
            })
    }
}
