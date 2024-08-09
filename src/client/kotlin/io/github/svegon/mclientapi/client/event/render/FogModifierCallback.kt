package io.github.svegon.mclientapi.client.event.render

import io.github.svegon.mclientapi.client.mixinterface.IStatusEffectFogModifier
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

interface FogModifierCallback {
    fun getFogModifier(
        entity: Entity, tickDelta: Float,
        callback: CallbackInfoReturnable<IStatusEffectFogModifier>
    )

    companion object {
        @JvmField
        val EVENT: Event<FogModifierCallback> = EventFactory.createArrayBacked(
            FogModifierCallback::class.java,
            object : FogModifierCallback {
                override fun getFogModifier(
                    entity: Entity,
                    tickDelta: Float,
                    callback: CallbackInfoReturnable<IStatusEffectFogModifier>,
                ) {
                }
            },
        ) { listeners: Array<FogModifierCallback> ->
            object : FogModifierCallback {
                override fun getFogModifier(
                    entity: Entity,
                    tickDelta: Float,
                    callback: CallbackInfoReturnable<IStatusEffectFogModifier>,
                ) {
                    for (listener in listeners) {
                        listener.getFogModifier(entity, tickDelta, callback)

                        if (callback.isCancelled) {
                            return
                        }
                    }
                }
            }
        }
    }
}
