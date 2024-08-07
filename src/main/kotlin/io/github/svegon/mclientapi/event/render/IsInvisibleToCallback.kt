package io.github.svegon.capi.event.render

import io.github.svegon.capi.event.render.IsInvisibleToCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function
import kotlin.Array
import kotlin.Boolean
import kotlin.invoke

interface IsInvisibleToCallback {
    fun invisibleSightCheck(entity: Entity?, viewer: PlayerEntity?, callback: CallbackInfoReturnable<Boolean?>?)

    companion object {
        @JvmField
        val EVENT: Event<IsInvisibleToCallback?> = EventFactory.createArrayBacked(
            IsInvisibleToCallback::class.java,
            IsInvisibleToCallback { entity: Entity?, viewer: PlayerEntity?, callback: CallbackInfoReturnable<Boolean?>? -> },
            Function { listeners: Array<IsInvisibleToCallback?>? ->
                IsInvisibleToCallback { entity: Entity?, viewer: PlayerEntity?, callback: CallbackInfoReturnable<Boolean?>? ->
                    for (listener in listeners) {
                        listener.invisibleSightCheck(entity, viewer, callback)

                        if (callback.isCancelled()) {
                            return@IsInvisibleToCallback
                        }
                    }
                }
            })
    }
}
