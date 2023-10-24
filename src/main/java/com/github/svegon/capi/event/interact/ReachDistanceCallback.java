package com.github.svegon.capi.event.interact;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerInteractionManager;

@FunctionalInterface
public interface ReachDistanceCallback {
    Event<ReachDistanceCallback> EVENT = EventFactory.createArrayBacked(ReachDistanceCallback.class,
            (interactionManager, unmodified) -> unmodified, listeners -> (interactionManager, unmodified) -> {
        for (ReachDistanceCallback listener : listeners) {
            float ret = listener.getReachDistance(interactionManager, unmodified);

            if (ret != unmodified) {
                return ret;
            }
        }

        return unmodified;
    });

    float getReachDistance(ClientPlayerInteractionManager interactionManager, float unmodified);
}
