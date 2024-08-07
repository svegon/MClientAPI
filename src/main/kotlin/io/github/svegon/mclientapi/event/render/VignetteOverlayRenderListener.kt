package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.Entity
import java.util.function.Function

interface VignetteOverlayRenderListener {
    fun onVignetteOverlayRender(context: DrawContext?, entity: Entity?, callback: CallbackInfo?)

    companion object {
        @JvmField
        val EVENT: Event<VignetteOverlayRenderListener?> =
            EventFactory.createArrayBacked<VignetteOverlayRenderListener?>(
                VignetteOverlayRenderListener::class.java,
                VignetteOverlayRenderListener { context: DrawContext?, entity: Entity?, callback: CallbackInfo? -> },
                Function<Array<VignetteOverlayRenderListener?>, VignetteOverlayRenderListener?> { listeners: Array<VignetteOverlayRenderListener?>? ->
                    VignetteOverlayRenderListener { context: DrawContext?, entity: Entity?, callback: CallbackInfo? ->
                        for (listener in listeners) {
                            listener.onVignetteOverlayRender(context, entity, callback)

                            if (callback.isCancelled()) {
                                return@VignetteOverlayRenderListener
                            }
                        }
                    }
                })
    }
}
