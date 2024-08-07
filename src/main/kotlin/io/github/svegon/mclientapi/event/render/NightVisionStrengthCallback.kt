package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.NightVisionStrengthCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Float
import kotlin.invoke

interface NightVisionStrengthCallback {
    fun getNightVisionStrength(entity: LivingEntity?, tickDelta: Float, callback: CallbackInfoReturnable<Float?>?)

    companion object {
        val EVENT: Event<NightVisionStrengthCallback?> = EventFactory.createArrayBacked(
            NightVisionStrengthCallback::class.java,
            NightVisionStrengthCallback { entity: LivingEntity?, tickDelta: Float, callback: CallbackInfoReturnable<Float?>? -> },
            Function { listeners: Array<NightVisionStrengthCallback?>? ->
                NightVisionStrengthCallback { entity: LivingEntity?, tickDelta: Float, callback: CallbackInfoReturnable<Float?>? ->
                    for (listener in listeners) {
                        listener.getNightVisionStrength(entity, tickDelta, callback)

                        if (callback.isCancelled()) {
                            return@NightVisionStrengthCallback
                        }
                    }
                }
            })
    }
}
