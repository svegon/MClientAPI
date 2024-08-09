package io.github.svegon.mclientapi.client.event.interaction

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.network.ClientPlayerInteractionManager

fun interface ReachDistanceCallback {
    fun getReachDistance(interactionManager: ClientPlayerInteractionManager?, unmodified: Float): Float

    companion object {
        @JvmField
        val EVENT: Event<ReachDistanceCallback> = EventFactory.createArrayBacked<ReachDistanceCallback>(
            ReachDistanceCallback::class.java,
            ReachDistanceCallback { interactionManager: ClientPlayerInteractionManager?, unmodified: Float -> unmodified }) { listeners: Array<ReachDistanceCallback> ->
            ReachDistanceCallback { interactionManager: ClientPlayerInteractionManager?, unmodified: Float ->
                for (listener in listeners) {
                    val ret = listener.getReachDistance(interactionManager, unmodified)

                    if (ret != unmodified) {
                        return@ReachDistanceCallback ret
                    }
                }
                unmodified
            }
        }
    }
}
