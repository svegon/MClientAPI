package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface StatusEffectLightCallback {
    Event<StatusEffectLightCallback> EVENT = EventFactory.createArrayBacked(StatusEffectLightCallback.class,
            (player, effect, callback) -> {}, listeners -> (player, effect, callback) -> {
        for (StatusEffectLightCallback listener : listeners) {
            listener.onLightStatusEffectCheck(player, effect, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onLightStatusEffectCheck(ClientPlayerEntity player, StatusEffect effect,
                                  CallbackInfoReturnable<Boolean> callback);
}
