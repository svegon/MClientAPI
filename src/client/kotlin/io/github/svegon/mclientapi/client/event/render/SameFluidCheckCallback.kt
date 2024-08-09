package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.fluid.FluidState
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.invoke

interface SameFluidCheckCallback {
    fun isSameFluid(rendered: FluidState, neighbor: FluidState, callback: CallbackInfoReturnable<Boolean>)

    companion object {
        @JvmField
        val EVENT: Event<SameFluidCheckCallback> = EventFactory.createArrayBacked(
            SameFluidCheckCallback::class.java,
            object : SameFluidCheckCallback {
                override fun isSameFluid(
                    rendered: FluidState,
                    neighbor: FluidState,
                    callback: CallbackInfoReturnable<Boolean>
                ) {}
            }
            ) {
            listeners -> object : SameFluidCheckCallback {
                override fun isSameFluid(
                    rendered: FluidState,
                    neighbor: FluidState,
                    callback: CallbackInfoReturnable<Boolean>
                ) {
                    for (listener in listeners) {
                        listener.isSameFluid(rendered, neighbor, callback)

                        if (callback.isCancelled) {
                            return
                        }
                    }
                }
            }
        }
    }
}
