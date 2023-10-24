package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@FunctionalInterface
public interface UnderwaterVisibilityCallback {
    Event<UnderwaterVisibilityCallback> EVENT = EventFactory.createArrayBacked(UnderwaterVisibilityCallback.class,
            (player, callback) -> {}, listeners -> (player, callback) -> {
        for (UnderwaterVisibilityCallback listener : listeners) {
            listener.onUnderwaterVisibility(player, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onUnderwaterVisibility(ClientPlayerEntity player, CallbackInfoReturnable<Float> callback);
}
