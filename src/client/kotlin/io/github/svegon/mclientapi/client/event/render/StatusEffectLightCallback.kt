package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.registry.entry.RegistryEntry
import java.util.function.Function

fun interface StatusEffectLightCallback {
    fun onLightStatusEffectCheck(
        player: ClientPlayerEntity, effect: RegistryEntry<StatusEffect>, present: Boolean
    ): Boolean

    companion object {
        @JvmField
        val EVENT: Event<StatusEffectLightCallback?> = EventFactory.createArrayBacked(
            StatusEffectLightCallback::class.java,
            StatusEffectLightCallback { player: ClientPlayerEntity, effect: RegistryEntry<StatusEffect>,
                                        present: Boolean -> present }
        ) { listeners: Array<StatusEffectLightCallback> ->
            StatusEffectLightCallback { player: ClientPlayerEntity, effect: RegistryEntry<StatusEffect>,
                                        present: Boolean ->
                for (listener in listeners) {
                    val ret = listener.onLightStatusEffectCheck(player, effect, present)

                    if (ret != present) {
                        return@StatusEffectLightCallback ret
                    }
                }

                present
            }
        }
    }
}
