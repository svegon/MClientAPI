package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.LightmapDarknessFactorCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Float
import kotlin.invoke

interface LightmapDarknessFactorCallback {
    fun getDarknessFactor(tickDelta: Float, callback: CallbackInfoReturnable<Float?>?)

    companion object {
        @JvmField
        val EVENT: Event<LightmapDarknessFactorCallback?> = EventFactory.createArrayBacked(
            LightmapDarknessFactorCallback::class.java,
            LightmapDarknessFactorCallback { tickDelta: Float, callback: CallbackInfoReturnable<Float?>? -> },
            Function { listeners: Array<LightmapDarknessFactorCallback?>? ->
                LightmapDarknessFactorCallback { tickDelta: Float, callback: CallbackInfoReturnable<Float?>? ->
                    for (listener in listeners) {
                        listener.getDarknessFactor(tickDelta, callback)

                        if (callback.isCancelled()) {
                            return@LightmapDarknessFactorCallback
                        }
                    }
                }
            })
    }
}
