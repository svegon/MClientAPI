package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

interface EntityTeamColorCallback {
    fun getEntityTeamColor(entity: Entity, callback: CallbackInfoReturnable<Int>)

    companion object {
        @JvmField
        val EVENT: Event<EntityTeamColorCallback?> = EventFactory.createArrayBacked(
            EntityTeamColorCallback::class.java,
            object : EntityTeamColorCallback {
                override fun getEntityTeamColor(entity: Entity, callback: CallbackInfoReturnable<Int>) {}
                                             },
            ) {
            listeners -> object : EntityTeamColorCallback {
                override fun getEntityTeamColor(entity: Entity, callback: CallbackInfoReturnable<Int>) {
                    for (listener in listeners) {
                        listener.getEntityTeamColor(entity, callback)

                        if (callback.isCancelled) {
                            return
                        }
                    }
                }
            }
        }
    }
}
