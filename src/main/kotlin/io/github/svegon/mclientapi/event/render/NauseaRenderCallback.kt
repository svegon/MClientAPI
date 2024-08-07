package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext

fun interface NauseaRenderCallback {
    fun onNauseaRender(
        gameRenderer: GameRenderer?, context: DrawContext?, distortionStrength: Float,
        callback: CallbackInfo?
    )

    companion object {
        val EVENT: Event<NauseaRenderCallback> = EventFactory.createArrayBacked<NauseaRenderCallback>(
            NauseaRenderCallback::class.java,
            NauseaRenderCallback { gameRenderer: GameRenderer?, context: DrawContext?, distortionStrength: Float, callback: CallbackInfo? -> }
        ) { listeners: Array<NauseaRenderCallback> ->
            NauseaRenderCallback { gameRenderer: GameRenderer?, context: DrawContext?, distortionStrength: Float, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.onNauseaRender(gameRenderer, context, distortionStrength, callback)

                    if (callback.isCancelled()) {
                        return@NauseaRenderCallback
                    }
                }
            }
        }
    }
}
