package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface VignetteOverlayRenderListener {
    Event<VignetteOverlayRenderListener> EVENT = EventFactory.createArrayBacked(VignetteOverlayRenderListener.class,
            (context, entity, callback) -> {}, listeners -> (context, entity, callback) -> {
        for (VignetteOverlayRenderListener listener : listeners) {
            listener.onVignetteOverlayRender(context, entity, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onVignetteOverlayRender(DrawContext context, Entity entity, CallbackInfo callback);
}
