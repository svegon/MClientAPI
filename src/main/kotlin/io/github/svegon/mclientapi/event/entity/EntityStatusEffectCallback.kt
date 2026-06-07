package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.LivingEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface EntityStatusEffectCallback {
    fun hasStatusEffect(entity: LivingEntity, effect: Holder<MobEffect>,
                        cir: CallbackInfoReturnable<Boolean>)

    companion object {
        val EVENT: Event<EntityStatusEffectCallback> = EventFactory.createArrayBacked(
            EntityStatusEffectCallback::class.java,
            EntityStatusEffectCallback { entity, effect, cir -> }) { listeners ->
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
