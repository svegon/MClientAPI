package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface IsInvisibleToCallback {
    Event<IsInvisibleToCallback> EVENT = EventFactory.createArrayBacked(IsInvisibleToCallback.class,
            (entity, viewer, callback) -> {}, (listeners) -> (entity, viewer, callback) -> {
        for (IsInvisibleToCallback listener : listeners) {
            listener.invisibleSightCheck(entity, viewer, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
            });

    void invisibleSightCheck(Entity entity, PlayerEntity viewer, CallbackInfoReturnable<Boolean> callback);
}
