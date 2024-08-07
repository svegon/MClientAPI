package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.InWallStateListener
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.invoke

interface InWallStateListener {
    fun onInWallStateGet(player: PlayerEntity?, callback: CallbackInfoReturnable<BlockState?>?)

    companion object {
        @JvmField
        val EVENT: Event<InWallStateListener?> = EventFactory.createArrayBacked(InWallStateListener::class.java,
            InWallStateListener { player: PlayerEntity?, callback: CallbackInfoReturnable<BlockState?>? -> },
            Function { listeners: Array<InWallStateListener?>? ->
                InWallStateListener { player: PlayerEntity?, callback: CallbackInfoReturnable<BlockState?>? ->
                    for (listener in listeners) {
                        listener.onInWallStateGet(player, callback)

                        if (callback.isCancelled()) {
                            return@InWallStateListener
                        }
                    }
                }
            })
    }
}
