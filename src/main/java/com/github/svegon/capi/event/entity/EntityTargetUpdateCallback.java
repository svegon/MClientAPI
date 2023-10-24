package com.github.svegon.capi.event.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

@FunctionalInterface
public interface EntityTargetUpdateCallback {
    Event<EntityTargetUpdateCallback> EVENT = EventFactory.createArrayBacked(EntityTargetUpdateCallback.class,
            (entity, min, max, box, predicate, reachSq, result) -> result,
            listeners -> (entity, min, max, box, predicate, reachSq, result) -> {
        for (EntityTargetUpdateCallback listener : listeners) {
            EntityHitResult override = listener.onEntityTargetUpdate(entity, min, max, box, predicate, reachSq, result);

            if (override != result) {
                return override;
            }
        }

        return result;
    });

    EntityHitResult onEntityTargetUpdate(Entity entity, Vec3d min, Vec3d max, Box box,
                                         Predicate<Entity> predicate, double reachSq, EntityHitResult result);
}
