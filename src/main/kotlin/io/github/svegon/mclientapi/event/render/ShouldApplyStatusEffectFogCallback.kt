package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.ShouldApplyStatusEffectFogCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.invoke

interface ShouldApplyStatusEffectFogCallback {
    fun shouldApplyStatusEffectFog(
        cameraEntity: LivingEntity?, effect: StatusEffect?,
        callback: CallbackInfoReturnable<Boolean?>?
    )

    companion object {
        @JvmField
        val EVENT: Event<ShouldApplyStatusEffectFogCallback?> = EventFactory.createArrayBacked(
            ShouldApplyStatusEffectFogCallback::class.java,
            ShouldApplyStatusEffectFogCallback { cameraEntity: LivingEntity?, effect: StatusEffect?, callback: CallbackInfoReturnable<Boolean?>? -> },
            Function { listeners: Array<ShouldApplyStatusEffectFogCallback?>? ->
                ShouldApplyStatusEffectFogCallback { cameraEntity: LivingEntity?, effect: StatusEffect?, callback: CallbackInfoReturnable<Boolean?>? ->
                    for (listener in listeners) {
                        listener.shouldApplyStatusEffectFog(cameraEntity, effect, callback)

                        if (callback.isCancelled()) {
                            return@ShouldApplyStatusEffectFogCallback
                        }
                    }
                }
            })
    }
}
