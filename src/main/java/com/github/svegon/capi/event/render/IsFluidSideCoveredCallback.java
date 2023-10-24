package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface IsFluidSideCoveredCallback {
    Event<IsFluidSideCoveredCallback> EVENT = EventFactory.createArrayBacked(IsFluidSideCoveredCallback.class,
            (world, direction, height, pos, state, callback) -> {},
            listeners -> (world, direction, height, pos, state, callback) -> {
        for (IsFluidSideCoveredCallback listener : listeners) {
            listener.isFluidSideCovered(world, direction, height, pos, state, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void isFluidSideCovered(BlockView world, Direction direction, float height, BlockPos pos,
                            BlockState state, CallbackInfoReturnable<Boolean> callback);
}
