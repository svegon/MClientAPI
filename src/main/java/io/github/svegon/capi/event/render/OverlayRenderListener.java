package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface OverlayRenderListener {
    Event<OverlayRenderListener> EVENT = EventFactory.createArrayBacked(OverlayRenderListener.class,
            (context, texture, scale, callback) -> {}, listeners -> (context, texture, scale, callback) -> {
        for (OverlayRenderListener listener : listeners) {
            listener.onOverlayRender(context, texture, scale, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onOverlayRender(DrawContext context, Identifier texture, float opacity, CallbackInfo callback);
}
