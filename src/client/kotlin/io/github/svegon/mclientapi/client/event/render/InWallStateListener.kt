package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.invoke

fun interface InWallStateListener {
    fun onInWallStateGet(player: PlayerEntity, callback: CallbackInfoReturnable<BlockState?>)

    companion object {
        @JvmField
        val EVENT: Event<InWallStateListener?> = EventFactory.createArrayBacked(InWallStateListener::class.java,
            InWallStateListener { player: PlayerEntity, callback: CallbackInfoReturnable<BlockState?> -> }
        ) { listeners: Array<InWallStateListener> ->
            InWallStateListener { player: PlayerEntity, callback: CallbackInfoReturnable<BlockState?> ->
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
