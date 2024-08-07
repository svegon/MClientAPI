package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.FogModifierCallback
import io.github.svegon.capi.mixininterface.IStatusEffectFogModifier
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Float
import kotlin.invoke

interface FogModifierCallback {
    fun getFogModifier(
        entity: Entity?, tickDelta: Float,
        callback: CallbackInfoReturnable<IStatusEffectFogModifier?>?
    )

    companion object {
        @JvmField
        val EVENT: Event<FogModifierCallback?> = EventFactory.createArrayBacked<FogModifierCallback?>(
            FogModifierCallback::class.java,
            FogModifierCallback { entity: Entity?, tickDelta: Float, callback: CallbackInfoReturnable<IStatusEffectFogModifier?>? -> },
            Function<Array<FogModifierCallback?>, FogModifierCallback?> { listeners: Array<FogModifierCallback?>? ->
                FogModifierCallback { entity: Entity?, tickDelta: Float, callback: CallbackInfoReturnable<IStatusEffectFogModifier?>? ->
                    for (listener in listeners) {
                        listener.getFogModifier(entity, tickDelta, callback)

                        if (callback.isCancelled()) {
                            return@FogModifierCallback
                        }
                    }
                }
            })
    }
}
