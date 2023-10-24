package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface SpyglassOverlayRenderListener {
    Event<SpyglassOverlayRenderListener> EVENT = EventFactory.createArrayBacked(SpyglassOverlayRenderListener.class,
            (context, scale, callback) -> {}, listeners -> (context, scale, callback) -> {
        for (SpyglassOverlayRenderListener listener : listeners) {
            listener.onSpyglassOverlayRender(context, scale, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onSpyglassOverlayRender(DrawContext context, float scale, CallbackInfo callback);
}
