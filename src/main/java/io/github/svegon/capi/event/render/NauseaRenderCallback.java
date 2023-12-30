package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@FunctionalInterface
public interface NauseaRenderCallback {
    Event<NauseaRenderCallback> EVENT = EventFactory.createArrayBacked(NauseaRenderCallback.class,
            (gameRenderer, context, distortionStrength, callback) -> {},
            listeners -> (gameRenderer, context, distortionStrength, callback) -> {
        for (NauseaRenderCallback listener : listeners) {
            listener.onNauseaRender(gameRenderer, context, distortionStrength, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onNauseaRender(GameRenderer gameRenderer, DrawContext context, float distortionStrength,
                        CallbackInfo callback);
}
