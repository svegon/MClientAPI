package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface NightVisionStrengthListener {
    fun getNightVisionStrength(entity: LivingEntity, tickDelta: Float, callback: CallbackInfoReturnable<Float>)

    companion object {
        @JvmField
        val EVENT: Event<NightVisionStrengthListener> = EventFactory.createArrayBacked(
            NightVisionStrengthListener::class.java,
            NightVisionStrengthListener { entity: LivingEntity, tickDelta: Float,
                                          callback: CallbackInfoReturnable<Float> -> }
        ) { listeners: Array<NightVisionStrengthListener> ->
            NightVisionStrengthListener { entity: LivingEntity, tickDelta: Float,
                                          callback: CallbackInfoReturnable<Float> ->
                for (listener in listeners) {
                    listener.getNightVisionStrength(entity, tickDelta, callback)

                    if (callback.isCancelled) {
                        return@NightVisionStrengthListener
                    }
                }
            }
        }
    }
}
