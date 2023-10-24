package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface NightVisionStrengthCallback {
    Event<NightVisionStrengthCallback> EVENT = EventFactory.createArrayBacked(NightVisionStrengthCallback.class,
            (entity, tickDelta, callback) -> {}, listeners -> (entity, tickDelta, callback) -> {
        for (NightVisionStrengthCallback listener : listeners) {
            listener.getNightVisionStrength(entity, tickDelta, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void getNightVisionStrength(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> callback);
}
