package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3
import java.util.function.Predicate

fun interface EntityTargetUpdateCallback {
    fun onEntityTargetUpdate(
        entity: Entity, min: Vec3, max: Vec3, box: AABB,
        predicate: Predicate<Entity>, reachSq: Double, result: EntityHitResult?
    ): EntityHitResult?

    companion object {
        val EVENT: Event<EntityTargetUpdateCallback> = EventFactory.createArrayBacked(
            EntityTargetUpdateCallback::class.java, EntityTargetUpdateCallback {
                entity: Entity, min: Vec3, max: Vec3, box: AABB, predicate: Predicate<Entity>,
                                         reachSq: Double, result: EntityHitResult? -> result }
        ) { listeners: Array<EntityTargetUpdateCallback> ->
            EntityTargetUpdateCallback { entity, min, max, box, predicate, reachSq, result ->
                for (listener in listeners) {
                    val override = listener.onEntityTargetUpdate(entity, min, max, box, predicate, reachSq, result)

                    if (override != result) {
                        return@EntityTargetUpdateCallback override
                    }
                }

                result
            }
        }
    }
}
