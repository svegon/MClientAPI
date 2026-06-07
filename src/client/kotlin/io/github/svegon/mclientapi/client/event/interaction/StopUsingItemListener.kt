package io.github.svegon.mclientapi.client.event.interaction

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.multiplayer.MultiPlayerGameMode
import net.minecraft.world.entity.player.Player
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface StopUsingItemListener {
    fun onStoppingUsingItem(
        interactionManager: MultiPlayerGameMode,
        player: Player,
        callback: CallbackInfo
    )

    companion object {
        val EVENT: Event<StopUsingItemListener> = EventFactory.createArrayBacked(
            StopUsingItemListener::class.java,
            StopUsingItemListener { interactionManager, player, ci: CallbackInfo -> })
        { listeners: Array<StopUsingItemListener> -> StopUsingItemListener {
            interactionManager: MultiPlayerGameMode, player: Player, ci: CallbackInfo ->
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
