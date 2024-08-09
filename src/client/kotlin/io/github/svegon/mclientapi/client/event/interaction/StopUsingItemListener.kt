package io.github.svegon.mclientapi.client.event.interaction

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface StopUsingItemListener {
    fun onStoppingUsingItem(
        interactionManager: ClientPlayerInteractionManager,
        player: PlayerEntity,
        ci: CallbackInfo
    )

    companion object {
        @JvmField
        val EVENT: Event<StopUsingItemListener> = EventFactory.createArrayBacked(
            StopUsingItemListener::class.java,
            StopUsingItemListener { interactionManager: ClientPlayerInteractionManager, player: PlayerEntity,
                                    ci: CallbackInfo -> }) { listeners: Array<StopUsingItemListener> ->
            StopUsingItemListener { interactionManager: ClientPlayerInteractionManager, player: PlayerEntity,
                                    ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.onStoppingUsingItem(interactionManager, player, ci)

                    if (ci.isCancellable) {
                        return@StopUsingItemListener
                    }
                }
            }
        }
    }
}
