package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import net.minecraft.entity.MovementType
import net.minecraft.util.math.Vec3d
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface EntityMoveListener {
    fun moveEntity(entity: Entity, type: MovementType, movement: Vec3d, ci: CallbackInfo)

    companion object {
        @JvmField
        val EVENT: Event<EntityMoveListener> = EventFactory.createArrayBacked(
            EntityMoveListener::class.java,
            EntityMoveListener { entity: Entity?, _: MovementType?, movement: Vec3d, ci: CallbackInfo -> }) { listeners: Array<EntityMoveListener> ->
            EntityMoveListener { entity: Entity, type: MovementType, movement: Vec3d, ci: CallbackInfo ->
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
