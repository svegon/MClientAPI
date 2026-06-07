package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface EntityTeamColorCallback {
    fun getEntityTeamColor(entity: Entity, callback: CallbackInfoReturnable<Int>)

    companion object {
        val EVENT: Event<EntityTeamColorCallback> = EventFactory.createArrayBacked(
            EntityTeamColorCallback::class.java,
            EntityTeamColorCallback { entity, callback -> }, ) {
            listeners -> EntityTeamColorCallback { entity: Entity, callback: CallbackInfoReturnable<Int> ->
                    for (listener in listeners) {
                        listener.getEntityTeamColor(entity, callback)

                        if (callback.isCancelled) {
                            return@EntityTeamColorCallback
                        }
                    }
                }
        }
    }
}
