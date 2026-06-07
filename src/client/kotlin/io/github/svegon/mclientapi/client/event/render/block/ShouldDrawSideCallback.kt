package io.github.svegon.mclientapi.client.event.render.block

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.BlockState
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface ShouldDrawSideCallback {
    fun shouldDrawSide(
        state: BlockState, neighbor: BlockState, direction: Direction,
        cir: CallbackInfoReturnable<Boolean>
    )

    companion object {
        val EVENT: Event<ShouldDrawSideCallback> = EventFactory.createArrayBacked(
            ShouldDrawSideCallback::class.java,
            ShouldDrawSideCallback { state: BlockState, neighbor: BlockState, direction: Direction,
                                     cir: CallbackInfoReturnable<Boolean> -> }
        ) { listeners: Array<ShouldDrawSideCallback> ->
            ShouldDrawSideCallback { state: BlockState, neighbor: BlockState, direction: Direction,
                                     cir: CallbackInfoReturnable<Boolean> ->
                for (listener in listeners) {
                    listener.shouldDrawSide(state, neighbor, direction, cir)

                    if (cir.isCancelled) {
                        return@ShouldDrawSideCallback
                    }
                }
            }
        }
    }
}
