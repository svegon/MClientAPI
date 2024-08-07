package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.network.ClientPlayerEntity
import java.util.function.Function

interface StatusEffectLightCallback {
    fun onLightStatusEffectCheck(
        player: ClientPlayerEntity?, effect: StatusEffect?,
        callback: CallbackInfoReturnable<Boolean?>?
    )

    companion object {
        @JvmField
        val EVENT: Event<StatusEffectLightCallback?> = EventFactory.createArrayBacked<StatusEffectLightCallback?>(
            StatusEffectLightCallback::class.java,
            StatusEffectLightCallback { player: ClientPlayerEntity?, effect: StatusEffect?, callback: CallbackInfoReturnable<Boolean?>? -> },
            Function<Array<StatusEffectLightCallback?>, StatusEffectLightCallback?> { listeners: Array<StatusEffectLightCallback?>? ->
                StatusEffectLightCallback { player: ClientPlayerEntity?, effect: StatusEffect?, callback: CallbackInfoReturnable<Boolean?>? ->
                    for (listener in listeners) {
                        listener.onLightStatusEffectCheck(player, effect, callback)

                        if (callback.isCancelled()) {
                            return@StatusEffectLightCallback
                        }
                    }
                }
            })
    }
}
