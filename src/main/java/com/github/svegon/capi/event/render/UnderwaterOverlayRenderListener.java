package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface UnderwaterOverlayRenderListener {
    Event<UnderwaterOverlayRenderListener> EVENT = EventFactory.createArrayBacked(UnderwaterOverlayRenderListener.class,
            (matrices, callback) -> {}, listeners -> (matrices, callback) -> {
        for (UnderwaterOverlayRenderListener listener : listeners) {
            listener.onUnderwaterOverlayRender(matrices, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onUnderwaterOverlayRender(MatrixStack matrices, CallbackInfo callback);
}
