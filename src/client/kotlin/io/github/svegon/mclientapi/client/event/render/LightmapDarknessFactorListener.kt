package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Float
import kotlin.invoke

fun interface LightmapDarknessFactorListener {
    fun getDarknessFactor(tickDelta: Float, callback: CallbackInfoReturnable<Float>)

    companion object {
        @JvmField
        val EVENT: Event<LightmapDarknessFactorListener?> = EventFactory.createArrayBacked(
            LightmapDarknessFactorListener::class.java,
            LightmapDarknessFactorListener { tickDelta: Float, callback: CallbackInfoReturnable<Float> -> }
        ) { listeners: Array<LightmapDarknessFactorListener> ->
            LightmapDarknessFactorListener { tickDelta: Float, callback: CallbackInfoReturnable<Float> ->
                for (listener in listeners) {
                    listener.getDarknessFactor(tickDelta, callback)

                    if (callback.isCancelled) {
                        return@LightmapDarknessFactorListener
                    }
                }
            }
        }
    }
}
