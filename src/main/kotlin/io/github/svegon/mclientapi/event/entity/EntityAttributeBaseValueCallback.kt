package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.registry.entry.RegistryEntry
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface EntityAttributeBaseValueCallback {
    fun interceptAttributeBaseValue(entity: LivingEntity, attribute: RegistryEntry<EntityAttribute>,
                                    cir: CallbackInfoReturnable<Double>)

    companion object {
        @JvmField
        val EVENT: Event<EntityAttributeBaseValueCallback?> = EventFactory.createArrayBacked(
            EntityAttributeBaseValueCallback::class.java,
            EntityAttributeBaseValueCallback { entity, attribute, cir -> }) {
            listeners ->
            EntityAttributeBaseValueCallback { entity, attribute, cir ->
                for (listener in listeners) {
                    listener.interceptAttributeBaseValue(entity, attribute, cir)

                    if (cir.isCancelled) {
                        return@EntityAttributeBaseValueCallback
                    }
                }
            }
        }
    }
}
