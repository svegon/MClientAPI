package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface SameFluidCheckCallback {
    Event<SameFluidCheckCallback> EVENT = EventFactory.createArrayBacked(SameFluidCheckCallback.class,
            (rendered, neighbor, callback) -> {}, listeners -> (rendered, neighbor, callback) -> {
        for (SameFluidCheckCallback listener : listeners) {
            listener.isSameFluid(rendered, neighbor, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void isSameFluid(FluidState rendered, FluidState neighbor, CallbackInfoReturnable<Boolean> callback);
}
