package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.invoke

fun interface EntityHasOutlineCallback {
    fun hasEntityOutline(entity: Entity, callback: CallbackInfoReturnable<Boolean>)

    companion object {
        @JvmField
        val EVENT: Event<EntityHasOutlineCallback> = EventFactory.createArrayBacked(
            EntityHasOutlineCallback::class.java,
            EntityHasOutlineCallback { entity: Entity, callback: CallbackInfoReturnable<Boolean> -> }
        ) { listeners: Array<EntityHasOutlineCallback> ->
            EntityHasOutlineCallback { entity: Entity, callback: CallbackInfoReturnable<Boolean> ->
                for (listener in listeners) {
                    listener.hasEntityOutline(entity, callback)

                    if (callback.isCancelled) {
                        return@EntityHasOutlineCallback
                    }
                }
            }
        }
    }
}
