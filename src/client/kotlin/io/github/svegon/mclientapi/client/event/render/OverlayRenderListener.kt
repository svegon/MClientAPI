package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface OverlayRenderListener {
    fun onOverlayRender(context: DrawContext, texture: Identifier, opacity: Float, callback: CallbackInfo)

    companion object {
        @JvmField
        val EVENT: Event<OverlayRenderListener> = EventFactory.createArrayBacked(
            OverlayRenderListener::class.java,
            OverlayRenderListener { context: DrawContext, texture: Identifier, scale: Float, callback: CallbackInfo -> }
        ) { listeners: Array<OverlayRenderListener> ->
            OverlayRenderListener { context: DrawContext, texture: Identifier, scale: Float, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.onOverlayRender(context, texture, scale, callback)

                    if (callback.isCancelled) {
                        return@OverlayRenderListener
                    }
                }
            }
        }
    }
}
