package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Float
import kotlin.invoke

interface EntityHorizontalSpeedMultiplierCallback {
    fun getEntityHorizontalSpeedMultiplier(entity: Entity?, cir: CallbackInfoReturnable<Float?>?)

    companion object {
        @JvmField
        val EVENT: Event<EntityHorizontalSpeedMultiplierCallback?> = EventFactory.createArrayBacked(
            EntityHorizontalSpeedMultiplierCallback::class.java,
            EntityHorizontalSpeedMultiplierCallback { entity: Entity?, cir: CallbackInfoReturnable<Float?>? -> },
            Function { listeners: Array<EntityHorizontalSpeedMultiplierCallback?>? ->
                EntityHorizontalSpeedMultiplierCallback { entity: Entity?, cir: CallbackInfoReturnable<Float?>? ->
                    for (listener in listeners) {
                        listener.getEntityHorizontalSpeedMultiplier(entity, cir)

                        if (cir.isCancelled()) {
                            return@EntityHorizontalSpeedMultiplierCallback
                        }
                    }
                }
            })
    }
}
