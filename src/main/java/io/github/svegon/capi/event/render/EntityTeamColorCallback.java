package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface EntityTeamColorCallback {
    Event<EntityTeamColorCallback> EVENT = EventFactory.createArrayBacked(EntityTeamColorCallback.class,
            (entity, callback) -> {}, listeners -> (entity, callback) -> {
        for (EntityTeamColorCallback listener : listeners) {
            listener.getEntityTeamColor(entity, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
            });

    void getEntityTeamColor(Entity entity, CallbackInfoReturnable<Integer> callback);
}
