package io.github.svegon.mclientapi.client.event.render.block

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface IsFluidSideCoveredCallback {
    fun isFluidSideCovered(
        direction: Direction, height: Float, state: BlockState, callback: CallbackInfoReturnable<Boolean>
    )

    companion object {
        val EVENT: Event<IsFluidSideCoveredCallback> = EventFactory.createArrayBacked(
            IsFluidSideCoveredCallback::class.java, IsFluidSideCoveredCallback {
                    direction: Direction, height: Float, state: BlockState, callback: CallbackInfoReturnable<Boolean> -> }
            ) { listeners: Array<IsFluidSideCoveredCallback> ->
            IsFluidSideCoveredCallback { direction, height, state, cir ->
                for (callback in listeners) {
                    callback.isFluidSideCovered(direction, height, state, cir)

                    if (cir.isCancelled) {
                        return@IsFluidSideCoveredCallback
                    }
                }
            }
        }
    }
}