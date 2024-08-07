package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.EntityHasOutlineCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.invoke

interface EntityHasOutlineCallback {
    fun hasEntityOutline(entity: Entity?, callback: CallbackInfoReturnable<Boolean?>?)

    companion object {
        @JvmField
        val EVENT: Event<EntityHasOutlineCallback?> = EventFactory.createArrayBacked(
            EntityHasOutlineCallback::class.java,
            EntityHasOutlineCallback { entity: Entity?, callback: CallbackInfoReturnable<Boolean?>? -> },
            Function { listeners: Array<EntityHasOutlineCallback?>? ->
                EntityHasOutlineCallback { entity: Entity?, callback: CallbackInfoReturnable<Boolean?>? ->
                    for (listener in listeners) {
                        listener.hasEntityOutline(entity, callback)

                        if (callback.isCancelled()) {
                            return@EntityHasOutlineCallback
                        }
                    }
                }
            })
    }
}
