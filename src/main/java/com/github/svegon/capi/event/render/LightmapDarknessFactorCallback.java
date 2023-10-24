package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface LightmapDarknessFactorCallback {
    Event<LightmapDarknessFactorCallback> EVENT = EventFactory.createArrayBacked(LightmapDarknessFactorCallback.class,
            (tickDelta, callback) -> {}, listeners -> (tickDelta, callback) -> {
        for (LightmapDarknessFactorCallback listener : listeners) {
            listener.getDarknessFactor(tickDelta, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
            });

    void getDarknessFactor(float tickDelta, CallbackInfoReturnable<Float> callback);
}
