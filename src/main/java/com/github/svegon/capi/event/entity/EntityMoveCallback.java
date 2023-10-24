package com.github.svegon.capi.event.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@FunctionalInterface
public interface EntityMoveCallback {
    Event<EntityMoveCallback> EVENT = EventFactory.createArrayBacked(EntityMoveCallback.class,
            (entity, movementType, movement, ci) -> {}, listeners -> (entity, movementType, movement, ci) -> {
        for (EntityMoveCallback listener : listeners) {
            listener.moveEntity(entity, movementType, movement, ci);

            if (ci.isCancelled()) {
                return;
            }
        }
    });

    void moveEntity(Entity entity, MovementType movementType, Vec3d movement, CallbackInfo ci);
}
