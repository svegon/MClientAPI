package io.github.svegon.mclientapi.client.event.render.block

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.level.material.FluidState
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface SameFluidCheckCallback {
    fun isSameFluid(rendered: FluidState, neighbor: FluidState, callback: CallbackInfoReturnable<Boolean>)

    companion object {
        val EVENT: Event<SameFluidCheckCallback> = EventFactory.createArrayBacked(
            SameFluidCheckCallback::class.java,
            SameFluidCheckCallback { rendered, neighbor, callback -> }
            ) {
            listeners -> SameFluidCheckCallback { rendered, neighbor, callback ->
                for (listener in listeners) {
                    listener.isSameFluid(rendered, neighbor, callback)

                    if (callback.isCancelled) {
                        return@SameFluidCheckCallback
                    }
                }
            }
        }
    }
}
