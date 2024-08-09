package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.registry.entry.RegistryEntry
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface EntityAttributeCallback {
    fun interceptAttributeValue(entity: LivingEntity, attribute: RegistryEntry<EntityAttribute>,
                                cir: CallbackInfoReturnable<Double>)

    companion object {
        @JvmField
        val EVENT: Event<EntityAttributeCallback?> = EventFactory.createArrayBacked(
            EntityAttributeCallback::class.java,
            object : EntityAttributeCallback {
                override fun interceptAttributeValue(
                    entity: LivingEntity,
                    attribute: RegistryEntry<EntityAttribute>,
                    cir: CallbackInfoReturnable<Double>
                ) {}
            }) {
            listeners -> object : EntityAttributeCallback {
                override fun interceptAttributeValue(
                    entity: LivingEntity,
                    attribute: RegistryEntry<EntityAttribute>,
                    cir: CallbackInfoReturnable<Double>
                ) {
                    for (listener in listeners) {
                        listener.interceptAttributeValue(entity, attribute, cir)

                        if (cir.isCancelled) {
                            return
                        }
                    }
                }
            }
        }
    }
}
