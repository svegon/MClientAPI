package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * floating item is the animation of the totem when it's reviving you
 */
public interface FloatingItemRenderListener {
    Event<FloatingItemRenderListener> EVENT = EventFactory.createArrayBacked(FloatingItemRenderListener.class,
            (scaledWidth, scaledHeight, tickDelta, callback) -> {},
            listeners -> (scaledWidth, scaledHeight, tickDelta, callback) -> {
        for (FloatingItemRenderListener listener : listeners) {
            listener.onFloatingItemRender(scaledWidth, scaledHeight, tickDelta, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onFloatingItemRender(int scaledWidth, int scaledHeight, float tickDelta, CallbackInfo callback);
}
