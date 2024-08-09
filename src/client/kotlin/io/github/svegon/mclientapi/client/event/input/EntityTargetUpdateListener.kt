package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import java.util.function.Predicate

fun interface EntityTargetUpdateListener {
    fun onEntityTargetUpdate(
        entity: Entity, min: Vec3d, max: Vec3d, box: Box,
        predicate: Predicate<Entity>, reachSq: Double, result: EntityHitResult?
    ): EntityHitResult?

    companion object {
        @JvmField
        val EVENT: Event<EntityTargetUpdateListener> = EventFactory.createArrayBacked(
            EntityTargetUpdateListener::class.java,
            EntityTargetUpdateListener { entity: Entity, min: Vec3d, max: Vec3d, box: Box, predicate: Predicate<Entity>,
                                         reachSq: Double, result: EntityHitResult? -> result }
        ) { listeners: Array<EntityTargetUpdateListener> ->
            EntityTargetUpdateListener { entity, min, max, box, predicate, reachSq, result ->
                for (listener in listeners) {
                    val override = listener.onEntityTargetUpdate(entity, min, max, box, predicate, reachSq, result)

                    if (override != result) {
                        return@EntityTargetUpdateListener override
                    }
                }

                result
            }
        }
    }
}
