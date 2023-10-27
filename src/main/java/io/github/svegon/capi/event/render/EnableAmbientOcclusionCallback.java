package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@FunctionalInterface
public interface EnableAmbientOcclusionCallback {
    Event<EnableAmbientOcclusionCallback> EVENT = EventFactory.createArrayBacked(
            EnableAmbientOcclusionCallback.class, callback -> {}, listeners -> callback -> {
                for (EnableAmbientOcclusionCallback listener : listeners) {
                    listener.isAmbientOcclusionEnabled(callback);

                    if (callback.isCancelled()) {
                        return;
                    }
                }
            });

    void isAmbientOcclusionEnabled(CallbackInfoReturnable<Boolean> callback);
}
