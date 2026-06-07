package io.github.svegon.mclientapi.client.event.render.light

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect

fun interface StatusEffectLightCallback {
    fun onLightingEffectCheck(
        player: LocalPlayer, effect: Holder<MobEffect>, present: Boolean
    ): Boolean

    companion object {
        val EVENT: Event<StatusEffectLightCallback> = EventFactory.createArrayBacked(
            StatusEffectLightCallback::class.java,
            StatusEffectLightCallback { player: LocalPlayer, effect: Holder<MobEffect>,
                                        present: Boolean -> present }
        ) { listeners: Array<StatusEffectLightCallback> ->
            StatusEffectLightCallback { player: LocalPlayer, effect: Holder<MobEffect>, present: Boolean ->
                for (listener in listeners) {
                    val ret = listener.onLightingEffectCheck(player, effect, present)

                    if (ret != present) {
                        return@StatusEffectLightCallback ret
                    }
                }

                present
            }
        }
    }
}