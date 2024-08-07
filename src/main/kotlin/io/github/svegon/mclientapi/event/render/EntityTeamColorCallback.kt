package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.EntityTeamColorCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Int
import kotlin.invoke

interface EntityTeamColorCallback {
    fun getEntityTeamColor(entity: Entity?, callback: CallbackInfoReturnable<Int?>?)

    companion object {
        @JvmField
        val EVENT: Event<EntityTeamColorCallback?> = EventFactory.createArrayBacked(
            EntityTeamColorCallback::class.java,
            EntityTeamColorCallback { entity: Entity?, callback: CallbackInfoReturnable<Int?>? -> },
            Function { listeners: Array<EntityTeamColorCallback?>? ->
                EntityTeamColorCallback { entity: Entity?, callback: CallbackInfoReturnable<Int?>? ->
                    for (listener in listeners) {
                        listener.getEntityTeamColor(entity, callback)

                        if (callback.isCancelled()) {
                            return@EntityTeamColorCallback
                        }
                    }
                }
            })
    }
}
