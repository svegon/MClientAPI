package io.github.svegon.mclientapi.client.event.render.overlay

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface InWallStateListener {
    fun onInWallStateGet(player: Player, callback: CallbackInfoReturnable<BlockState?>)

    companion object {
        val EVENT: Event<InWallStateListener> = EventFactory.createArrayBacked(InWallStateListener::class.java,
            InWallStateListener { player: Player, callback: CallbackInfoReturnable<BlockState?> -> }
        ) { listeners: Array<InWallStateListener> ->
            InWallStateListener { player: Player, callback: CallbackInfoReturnable<BlockState?> ->
                for (listener in listeners) {
                    listener.onInWallStateGet(player, callback)

                    if (callback.isCancelled) {
                        return@InWallStateListener
                    }
                }
            }
        }
    }
}
