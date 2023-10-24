package com.github.svegon.capi.event.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface EntityHorizontalSpeedMultiplierCallback {
    Event<EntityHorizontalSpeedMultiplierCallback> EVENT =
            EventFactory.createArrayBacked(EntityHorizontalSpeedMultiplierCallback.class,
            (entity, cir) -> {}, listeners -> (entity, cir) -> {
        for (EntityHorizontalSpeedMultiplierCallback listener : listeners) {
            listener.getEntityHorizontalSpeedMultiplier(entity, cir);

            if (cir.isCancelled()) {
                return;
            }
        }
    });

    void getEntityHorizontalSpeedMultiplier(Entity entity, CallbackInfoReturnable<Float> cir);
}
