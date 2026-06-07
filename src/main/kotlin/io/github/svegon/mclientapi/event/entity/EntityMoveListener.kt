package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.MoverType
import net.minecraft.world.phys.Vec3
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface EntityMoveListener {
    fun moveEntity(entity: Entity, type: MoverType, movement: Vec3, ci: CallbackInfo)

    companion object {
        val EVENT: Event<EntityMoveListener> = EventFactory.createArrayBacked(EntityMoveListener::class.java,
            EntityMoveListener { entity, _, movement, ci -> }) { listeners: Array<EntityMoveListener> ->
            EntityMoveListener { entity: Entity, type: MoverType, movement: Vec3, ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.moveEntity(entity, type, movement, ci)

                    if (ci.isCancelled) {
                        return@EntityMoveListener
                    }
                }
            }
        }
    }
}
