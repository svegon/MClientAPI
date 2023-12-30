package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface LoadingOverlayRenderListener {
    Event<LoadingOverlayRenderListener> EVENT = EventFactory.createArrayBacked(LoadingOverlayRenderListener.class,
            (overlay, matrices, mouseX, mouseY, lastFrameDuration, callback) -> {},
            listeners -> (overlay, matrices, mouseX, mouseY, lastFrameDuration, callback) -> {
        for (LoadingOverlayRenderListener listener : listeners) {
            listener.onLoadingOverlayRender(overlay, matrices, mouseX, mouseY, lastFrameDuration, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onLoadingOverlayRender(Overlay overlay, DrawContext context, int mouseX, int mouseY, float tickDelta,
                                CallbackInfo callback);
}
