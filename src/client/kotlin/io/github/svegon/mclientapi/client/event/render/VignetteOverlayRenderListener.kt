package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

fun interface VignetteOverlayRenderListener {
    fun onVignetteOverlayRender(context: DrawContext, entity: Entity?, callback: CallbackInfo)

    companion object {
        @JvmField
        val EVENT: Event<VignetteOverlayRenderListener> = EventFactory.createArrayBacked(
                VignetteOverlayRenderListener::class.java,
                VignetteOverlayRenderListener { context: DrawContext, entity: Entity?, callback: CallbackInfo -> }
        ) { listeners: Array<VignetteOverlayRenderListener> ->
            VignetteOverlayRenderListener { context: DrawContext, entity: Entity?, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.onVignetteOverlayRender(context, entity, callback)

                    if (callback.isCancelled) {
                        return@VignetteOverlayRenderListener
                    }
                }
            }
        }
    }
}
