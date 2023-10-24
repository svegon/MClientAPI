package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface BlockRenderTypeCallback {
    Event<BlockRenderTypeCallback> EVENT = EventFactory.createArrayBacked(BlockRenderTypeCallback.class,
            (state, callback) -> {}, listeners -> (state, callback) -> {
        for (BlockRenderTypeCallback listener : listeners) {
            listener.getBlockRenderType(state, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void getBlockRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> callback);
}
