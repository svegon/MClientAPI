package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.registry.entry.RegistryEntry
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.invoke

fun interface EntityStatusEffectCallback {
    fun hasStatusEffect(entity: LivingEntity, effect: RegistryEntry<StatusEffect>,
                        cir: CallbackInfoReturnable<Boolean>)

    companion object {
        @JvmField
        val EVENT: Event<EntityStatusEffectCallback?> = EventFactory.createArrayBacked(
            EntityStatusEffectCallback::class.java,
            EntityStatusEffectCallback { entity, effect, cir -> }) {
            listeners ->
            EntityStatusEffectCallback { entity, effect, cir ->
                for (listener in listeners) {
                    listener.hasStatusEffect(entity, effect, cir)

                    if (cir.isCancelled) {
                        return@EntityStatusEffectCallback
                    }
                }
            }
        }
    }
}
