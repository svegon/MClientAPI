package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attribute
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface EntityAttributeBaseValueCallback {
    fun interceptAttributeBaseValue(entity: LivingEntity, attribute: Holder<Attribute>,
                                    cir: CallbackInfoReturnable<Double>)

    companion object {
        val EVENT: Event<EntityAttributeBaseValueCallback> = EventFactory.createArrayBacked(
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
