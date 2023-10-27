package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface ShouldDrawSideCallback {
    Event<ShouldDrawSideCallback> EVENT = EventFactory.createArrayBacked(ShouldDrawSideCallback.class, (state, world,
                pos, side, otherPos, callback) -> {}, listeners -> (state, world, pos, side, neighbor, callback) -> {
        for (ShouldDrawSideCallback listener : listeners) {
            listener.shouldDrawSide(state, world, pos, side, neighbor, callback);
        }
    });

    void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos neighbor,
                        CallbackInfoReturnable<Boolean> callback);
}
