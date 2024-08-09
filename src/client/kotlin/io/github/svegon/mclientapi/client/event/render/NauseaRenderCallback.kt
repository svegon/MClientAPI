package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.GameRenderer
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface NauseaRenderCallback {
    fun onNauseaRender(
        gameRenderer: GameRenderer, context: DrawContext, distortionStrength: Float,
        callback: CallbackInfo
    )

    companion object {
        @JvmField
        val EVENT: Event<NauseaRenderCallback> = EventFactory.createArrayBacked<NauseaRenderCallback>(
            NauseaRenderCallback::class.java,
            NauseaRenderCallback { gameRenderer: GameRenderer, context: DrawContext, distortionStrength: Float,
                                   callback: CallbackInfo? -> }
        ) { listeners: Array<NauseaRenderCallback> ->
            NauseaRenderCallback { gameRenderer: GameRenderer, context: DrawContext, distortionStrength: Float,
                                   callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.onNauseaRender(gameRenderer, context, distortionStrength, callback)

                    if (callback.isCancelled) {
                        return@NauseaRenderCallback
                    }
                }
            }
        }
    }
}
