package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.FloatingItemRenderListener
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function
import kotlin.Array
import kotlin.Float
import kotlin.Int
import kotlin.invoke

/**
 * floating item is the animation of the totem when it's reviving you
 */
interface FloatingItemRenderListener {
    fun onFloatingItemRender(scaledWidth: Int, scaledHeight: Int, tickDelta: Float, callback: CallbackInfo?)

    companion object {
        val EVENT: Event<FloatingItemRenderListener?> = EventFactory.createArrayBacked(
            FloatingItemRenderListener::class.java,
            FloatingItemRenderListener { scaledWidth: Int, scaledHeight: Int, tickDelta: Float, callback: CallbackInfo? -> },
            Function { listeners: Array<FloatingItemRenderListener?>? ->
                FloatingItemRenderListener { scaledWidth: Int, scaledHeight: Int, tickDelta: Float, callback: CallbackInfo? ->
                    for (listener in listeners) {
                        listener.onFloatingItemRender(scaledWidth, scaledHeight, tickDelta, callback)

                        if (callback.isCancelled()) {
                            return@FloatingItemRenderListener
                        }
                    }
                }
            })
    }
}
