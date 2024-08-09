package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.GameRenderer
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function
import kotlin.Array
import kotlin.Float
import kotlin.Int
import kotlin.invoke

/**
 * floating item is the animation of the totem when it's reviving you
 */
fun interface FloatingItemRenderListener {
    fun onFloatingItemRender(renderer: GameRenderer, context: DrawContext, tickDelta: Float, callback: CallbackInfo)

    companion object {
        @JvmField
        val EVENT: Event<FloatingItemRenderListener> = EventFactory.createArrayBacked(
            FloatingItemRenderListener::class.java,
            FloatingItemRenderListener { renderer: GameRenderer, context: DrawContext, tickDelta: Float,
                                         callback: CallbackInfo -> }
        ) { listeners: Array<FloatingItemRenderListener> ->
            FloatingItemRenderListener { renderer: GameRenderer, context: DrawContext, tickDelta: Float,
                                         callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.onFloatingItemRender(renderer, context, tickDelta, callback)

                    if (callback.isCancelled) {
                        return@FloatingItemRenderListener
                    }
                }
            }
        }
    }
}
