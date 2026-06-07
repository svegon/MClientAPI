package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface EntityAttributeCallback {
    fun interceptAttributeValue(entity: LivingEntity, attribute: Holder<Attribute>,
                                cir: CallbackInfoReturnable<Double>)

    companion object {
        val EVENT: Event<EntityAttributeCallback> = EventFactory.createArrayBacked(
            EntityAttributeCallback::class.java,
            EntityAttributeCallback { entity, attribute, cir -> }) {
            listeners ->
            EntityAttributeCallback { entity, attribute, cir ->
                for (listener in listeners) {
                    listener.interceptAttributeValue(entity, attribute, cir)

                    if (cir.isCancelled) {
                        return@EntityAttributeCallback
                    }
                }
            }
        }
    }
}
