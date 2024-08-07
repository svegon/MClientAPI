package io.github.svegon.capi.event.entity

import io.github.svegon.capi.event.entity.EntityStatusEffectCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.invoke

interface EntityStatusEffectCallback {
    fun hasStatusEffect(entity: LivingEntity?, effect: StatusEffect?, cir: CallbackInfoReturnable<Boolean?>?)

    companion object {
        @JvmField
        val EVENT: Event<EntityStatusEffectCallback?> = EventFactory.createArrayBacked(
            EntityStatusEffectCallback::class.java,
            EntityStatusEffectCallback { entity: LivingEntity?, effect: StatusEffect?, cir: CallbackInfoReturnable<Boolean?>? -> },
            Function { listeners: Array<EntityStatusEffectCallback?>? ->
                EntityStatusEffectCallback { entity: LivingEntity?, effect: StatusEffect?, cir: CallbackInfoReturnable<Boolean?>? ->
                    for (listener in listeners) {
                        listener.hasStatusEffect(entity, effect, cir)

                        if (cir.isCancelled()) {
                            return@EntityStatusEffectCallback
                        }
                    }
                }
            })
    }
}
