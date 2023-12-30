package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface ShouldRenderBlockOutlineListener {
    Event<ShouldRenderBlockOutlineListener> EVENT = EventFactory.createArrayBacked(
            ShouldRenderBlockOutlineListener.class, (renderer, callback) -> {}, listeners -> (renderer, callback) -> {
                for (ShouldRenderBlockOutlineListener listener : listeners) {
                    listener.onShouldRenderBlockOutline(renderer, callback);

                    if (callback.isCancelled()) {
                        return;
                    }
                }
            });

    void onShouldRenderBlockOutline(GameRenderer renderer, CallbackInfoReturnable<Boolean> callback);
}
