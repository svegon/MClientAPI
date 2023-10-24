package com.github.svegon.capi.event.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface EntityStatusEffectCallback {
    Event<EntityStatusEffectCallback> EVENT = EventFactory.createArrayBacked(EntityStatusEffectCallback.class,
            (entity, effect, cir) -> {}, listeners -> (entity, effect, cir) -> {
        for (EntityStatusEffectCallback listener : listeners) {
            listener.hasStatusEffect(entity, effect, cir);

            if (cir.isCancelled()) {
                return;
            }
        }
    });

    void hasStatusEffect(LivingEntity entity, StatusEffect effect, CallbackInfoReturnable<Boolean> cir);
}
