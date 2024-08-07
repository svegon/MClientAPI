package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.RenderListener
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.invoke

interface RenderListener {
    fun onRender(tick: Boolean, callback: CallbackInfo?)

    companion object {
        @JvmField
        val EVENT: Event<RenderListener?> = EventFactory.createArrayBacked(
            RenderListener::class.java,
            Function { listeners: Array<RenderListener?>? ->
                RenderListener { tick: Boolean, callback: CallbackInfo? ->
                    for (listener in listeners) {
                        listener.onRender(tick, callback)

                        if (callback.isCancelled()) {
                            return@RenderListener
                        }
                    }
                }
            })
    }
}
