package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface RenderListener {
    Event<RenderListener> EVENT = EventFactory.createArrayBacked(RenderListener.class,
            listeners -> (tick, callback) -> {
        for (RenderListener listener : listeners) {
            listener.onRender(tick, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onRender(boolean tick, CallbackInfo callback);
}
