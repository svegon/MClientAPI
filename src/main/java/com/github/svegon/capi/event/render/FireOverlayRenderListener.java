package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface FireOverlayRenderListener {
    Event<FireOverlayRenderListener> EVENT = EventFactory.createArrayBacked(FireOverlayRenderListener.class,
            (matrices, callback) -> {}, listeners -> (matrices, callback) -> {
        for (FireOverlayRenderListener listener : listeners) {
            listener.onFireOverlayRender(matrices, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onFireOverlayRender(MatrixStack matrices, CallbackInfo callback);
}
