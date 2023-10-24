package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface InWallStateListener {
    Event<InWallStateListener> EVENT = EventFactory.createArrayBacked(InWallStateListener.class,
            (player, callback) -> {}, listeners -> (player, callback) -> {
        for (InWallStateListener listener : listeners) {
            listener.onInWallStateGet(player, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onInWallStateGet(PlayerEntity player, CallbackInfoReturnable<BlockState> callback);
}
