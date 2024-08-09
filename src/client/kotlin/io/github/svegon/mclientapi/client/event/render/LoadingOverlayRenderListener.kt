package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Overlay
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

fun interface LoadingOverlayRenderListener {
    fun onLoadingOverlayRender(
        overlay: Overlay, context: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float,
        callback: CallbackInfo
    )

    companion object {
        @JvmField
        val EVENT: Event<LoadingOverlayRenderListener> = EventFactory.createArrayBacked(
            LoadingOverlayRenderListener::class.java,
            LoadingOverlayRenderListener { overlay: Overlay, matrices: DrawContext, mouseX: Int, mouseY: Int,
                                           lastFrameDuration: Float, callback: CallbackInfo -> }
        ) { listeners: Array<LoadingOverlayRenderListener> ->
            LoadingOverlayRenderListener { overlay: Overlay, matrices: DrawContext, mouseX: Int, mouseY: Int,
                                           lastFrameDuration: Float, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.onLoadingOverlayRender(overlay, matrices, mouseX, mouseY, lastFrameDuration, callback)

                    if (callback.isCancelled) {
                        return@LoadingOverlayRenderListener
                    }
                }
            }
        }
    }
}
