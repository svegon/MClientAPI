package io.github.svegon.capi.event.interact

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.network.ClientPlayerInteractionManager

fun interface StopUsingItemListener {
    fun onStoppingUsingItem(
        interactionManager: ClientPlayerInteractionManager?,
        player: PlayerEntity?,
        ci: CallbackInfo?
    )

    companion object {
        @JvmField
        val EVENT: Event<StopUsingItemListener> = EventFactory.createArrayBacked<StopUsingItemListener>(
            StopUsingItemListener::class.java,
            StopUsingItemListener { interactionManager: ClientPlayerInteractionManager?, player: PlayerEntity?, ci: CallbackInfo? -> }) { listeners: Array<StopUsingItemListener> ->
            StopUsingItemListener { interactionManager: ClientPlayerInteractionManager?, player: PlayerEntity?, ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.onStoppingUsingItem(interactionManager, player, ci)

                    if (ci.isCancellable()) {
                        return@StopUsingItemListener
                    }
                }
            }
        }
    }
}
