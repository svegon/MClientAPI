package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface ShouldEntityRenderCallback {
    Event<ShouldEntityRenderCallback> EVENT = EventFactory.createArrayBacked(ShouldEntityRenderCallback.class,
            (entity, frustum, camera, callback) -> {}, listeners -> (entity, frustum, camera, callback) -> {
        for (ShouldEntityRenderCallback listener : listeners) {
            listener.shouldRenderEntity(entity, frustum, camera, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
            });

    void shouldRenderEntity(Entity entity, Frustum frustum, Camera camera, CallbackInfoReturnable<Boolean> callback);
}
