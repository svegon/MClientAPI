package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface IsInvisibleToListener {
    fun invisibleSightCheck(entity: Entity, viewer: Player, callback: CallbackInfoReturnable<Boolean>)

    companion object {
        val EVENT: Event<IsInvisibleToListener> = EventFactory.createArrayBacked(IsInvisibleToListener::class.java,
            IsInvisibleToListener { entity, viewer, callback -> }) { listeners ->
            IsInvisibleToListener { entity, viewer, callback ->
                for (listener in listeners) {
                    listener.invisibleSightCheck(entity, viewer, callback)
                }
            }
        }
    }
}
