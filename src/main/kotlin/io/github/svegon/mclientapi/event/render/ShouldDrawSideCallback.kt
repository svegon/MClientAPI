package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.ShouldDrawSideCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BlockState
import net.minecraft.util.math.*
import net.minecraft.world.BlockView
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.invoke

interface ShouldDrawSideCallback {
    fun shouldDrawSide(
        state: BlockState?, world: BlockView?, pos: BlockPos?, side: Direction?, neighbor: BlockPos?,
        callback: CallbackInfoReturnable<Boolean?>?
    )

    companion object {
        @JvmField
        val EVENT: Event<ShouldDrawSideCallback?> = EventFactory.createArrayBacked(
            ShouldDrawSideCallback::class.java,
            ShouldDrawSideCallback { state: BlockState?, world: BlockView?, pos: BlockPos?, side: Direction?, otherPos: BlockPos?, callback: CallbackInfoReturnable<Boolean?>? -> },
            Function { listeners: Array<ShouldDrawSideCallback?>? ->
                ShouldDrawSideCallback { state: BlockState?, world: BlockView?, pos: BlockPos?, side: Direction?, neighbor: BlockPos?, callback: CallbackInfoReturnable<Boolean?>? ->
                    for (listener in listeners) {
                        listener.shouldDrawSide(state, world, pos, side, neighbor, callback)
                    }
                }
            })
    }
}
