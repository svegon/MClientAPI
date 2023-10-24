package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface EntityHasOutlineCallback {
    Event<EntityHasOutlineCallback> EVENT = EventFactory.createArrayBacked(EntityHasOutlineCallback.class,
            (entity, callback) -> {}, listeners -> (entity, callback) -> {
        for (EntityHasOutlineCallback listener : listeners) {
            listener.hasEntityOutline(entity, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
            });

    void hasEntityOutline(Entity entity, CallbackInfoReturnable<Boolean> callback);
}
