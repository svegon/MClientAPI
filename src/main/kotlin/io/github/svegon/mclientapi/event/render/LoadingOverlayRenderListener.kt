package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import java.util.function.Function

interface LoadingOverlayRenderListener {
    fun onLoadingOverlayRender(
        overlay: Overlay?, context: DrawContext?, mouseX: Int, mouseY: Int, tickDelta: Float,
        callback: CallbackInfo?
    )

    companion object {
        val EVENT: Event<LoadingOverlayRenderListener?> = EventFactory.createArrayBacked<LoadingOverlayRenderListener?>(
            LoadingOverlayRenderListener::class.java,
            LoadingOverlayRenderListener { overlay: Overlay?, matrices: DrawContext?, mouseX: Int, mouseY: Int, lastFrameDuration: Float, callback: CallbackInfo? -> },
            Function<Array<LoadingOverlayRenderListener?>, LoadingOverlayRenderListener?> { listeners: Array<LoadingOverlayRenderListener?>? ->
                LoadingOverlayRenderListener { overlay: Overlay?, matrices: DrawContext?, mouseX: Int, mouseY: Int, lastFrameDuration: Float, callback: CallbackInfo? ->
                    for (listener in listeners) {
                        listener.onLoadingOverlayRender(overlay, matrices, mouseX, mouseY, lastFrameDuration, callback)

                        if (callback.isCancelled()) {
                            return@LoadingOverlayRenderListener
                        }
                    }
                }
            })
    }
}
