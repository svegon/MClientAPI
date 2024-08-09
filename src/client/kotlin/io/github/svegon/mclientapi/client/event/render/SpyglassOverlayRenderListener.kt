package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

fun interface SpyglassOverlayRenderListener {
    fun onSpyglassOverlayRender(context: DrawContext, scale: Float, callback: CallbackInfo)

    companion object {
        @JvmField
        val EVENT: Event<SpyglassOverlayRenderListener> =
            EventFactory.createArrayBacked(SpyglassOverlayRenderListener::class.java,
                SpyglassOverlayRenderListener { context: DrawContext, scale: Float, callback: CallbackInfo -> }
            ) { listeners: Array<SpyglassOverlayRenderListener> ->
                SpyglassOverlayRenderListener { context: DrawContext, scale: Float, callback: CallbackInfo ->
                    for (listener in listeners) {
                        listener.onSpyglassOverlayRender(context, scale, callback)

                        if (callback.isCancelled) {
                            return@SpyglassOverlayRenderListener
                        }
                    }
                }
            }
    }
}
