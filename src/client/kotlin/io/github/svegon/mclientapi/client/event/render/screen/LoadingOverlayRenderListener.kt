package io.github.svegon.mclientapi.client.event.render.screen

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Overlay
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface LoadingOverlayRenderListener {
    fun onLoadingOverlayRender(
        overlay: Overlay, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, tickDelta: Float,
        callback: CallbackInfo
    )

    companion object {
        val EVENT: Event<LoadingOverlayRenderListener> = EventFactory.createArrayBacked(
            LoadingOverlayRenderListener::class.java,
            LoadingOverlayRenderListener { overlay: Overlay, graphics: GuiGraphicsExtractor,
                                           mouseX: Int, mouseY: Int, lastFrameDuration: Float,
                                           callback: CallbackInfo -> }
        ) { listeners: Array<LoadingOverlayRenderListener> ->
            LoadingOverlayRenderListener { overlay: Overlay, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int,
                                           lastFrameDuration: Float, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.onLoadingOverlayRender(overlay, graphics, mouseX, mouseY,
                        lastFrameDuration, callback)

                    if (callback.isCancelled) {
                        return@LoadingOverlayRenderListener
                    }
                }
            }
        }
    }
}