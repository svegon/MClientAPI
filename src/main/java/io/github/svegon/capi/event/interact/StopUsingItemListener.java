package io.github.svegon.capi.event.interact;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@FunctionalInterface
public interface StopUsingItemListener {
    Event<StopUsingItemListener> EVENT = EventFactory.createArrayBacked(StopUsingItemListener.class,
            (interactionManager, player, ci) -> {}, listeners -> (interactionManager, player, ci) -> {
        for (StopUsingItemListener listener : listeners) {
            listener.onStoppingUsingItem(interactionManager, player, ci);

            if (ci.isCancellable()) {
                return;
            }
        }
    });

    void onStoppingUsingItem(ClientPlayerInteractionManager interactionManager, PlayerEntity player, CallbackInfo ci);
}
