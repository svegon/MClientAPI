package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.BlockState
import net.minecraft.util.math.*
import net.minecraft.world.BlockView
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.Float
import kotlin.invoke

fun interface IsFluidSideCoveredCallback {
    fun isFluidSideCovered(
        world: BlockView, direction: Direction, height: Float, pos: BlockPos,
        state: BlockState, callback: CallbackInfoReturnable<Boolean>
    )

    companion object {
        @JvmField
        val EVENT: Event<IsFluidSideCoveredCallback> = EventFactory.createArrayBacked(
            IsFluidSideCoveredCallback::class.java,
            IsFluidSideCoveredCallback { world, direction, height, pos, state, callback -> }
            ) {
            listeners: Array<IsFluidSideCoveredCallback> -> object : IsFluidSideCoveredCallback {
            override fun isFluidSideCovered(
                world: BlockView,
                direction: Direction,
                height: Float,
                pos: BlockPos,
                state: BlockState,
                callback: CallbackInfoReturnable<Boolean>
            ) {
                for (listener in listeners) {
                    listener.isFluidSideCovered(world, direction, height, pos, state, callback)

                    if (callback.isCancelled) {
                        return
                    }
                }
            }
            }
        }
    }
}
