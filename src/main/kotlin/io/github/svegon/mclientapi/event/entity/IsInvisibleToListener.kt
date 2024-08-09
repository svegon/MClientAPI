package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

interface IsInvisibleToListener {
    fun invisibleSightCheck(entity: Entity, viewer: PlayerEntity, callback: CallbackInfoReturnable<Boolean>)

    companion object {
        @JvmField
        val EVENT: Event<IsInvisibleToListener?> = EventFactory.createArrayBacked(
            IsInvisibleToListener::class.java,
            object : IsInvisibleToListener {
                override fun invisibleSightCheck(
                    entity: Entity,
                    viewer: PlayerEntity,
                    callback: CallbackInfoReturnable<Boolean>
                ) {}
            }) {
            listeners -> object : IsInvisibleToListener {
                override fun invisibleSightCheck(
                    entity: Entity,
                    viewer: PlayerEntity,
                    callback: CallbackInfoReturnable<Boolean>
                ) {
                    for (listener in listeners) listener.invisibleSightCheck(entity, viewer, callback)
                }
            }
        }
    }
}
