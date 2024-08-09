package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.util.math.MatrixStack
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

fun interface FireOverlayRenderListener {
    fun onFireOverlayRender(matrices: MatrixStack, callback: CallbackInfo)

    companion object {
        @JvmField
        val EVENT: Event<FireOverlayRenderListener?> = EventFactory.createArrayBacked(
            FireOverlayRenderListener::class.java,
            FireOverlayRenderListener { matrices: MatrixStack, callback: CallbackInfo -> }
        ) { listeners: Array<FireOverlayRenderListener> ->
            FireOverlayRenderListener { matrices: MatrixStack, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.onFireOverlayRender(matrices, callback)

                    if (callback.isCancelled) {
                        return@FireOverlayRenderListener
                    }
                }
            }
        }
    }
}
