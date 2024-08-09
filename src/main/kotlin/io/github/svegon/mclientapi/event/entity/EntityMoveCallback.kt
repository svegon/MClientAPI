package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import net.minecraft.entity.MovementType
import net.minecraft.util.math.Vec3d
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface EntityMoveCallback {
    fun moveEntity(entity: Entity?, movementType: MovementType?, movement: Vec3d?, ci: CallbackInfo?)

    companion object {
        @JvmField
        val EVENT: Event<EntityMoveCallback> = EventFactory.createArrayBacked(
            EntityMoveCallback::class.java,
            EntityMoveCallback { entity: Entity?, movementType: MovementType?, movement: Vec3d?, ci: CallbackInfo? -> }) { listeners: Array<EntityMoveCallback> ->
            EntityMoveCallback { entity: Entity?, movementType: MovementType?, movement: Vec3d?, ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.moveEntity(entity, movementType, movement, ci)

                    if (ci.isCancelled) {
                        return@EntityMoveCallback
                    }
                }
            }
        }
    }
}
