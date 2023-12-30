package io.github.svegon.capi.event.input;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface CrosshairTargetUpdateListener {
    Event<CrosshairTargetUpdateListener> EVENT = EventFactory.createArrayBacked(CrosshairTargetUpdateListener.class,
            (client, tickDelta) -> {}, listeners -> (client, tickDelta) -> {
        for (CrosshairTargetUpdateListener listener : listeners) {
            listener.onCrosshairTargetUpdate(client, tickDelta);
        }
            });

    void onCrosshairTargetUpdate(MinecraftClient client, float tickDelta);
}
