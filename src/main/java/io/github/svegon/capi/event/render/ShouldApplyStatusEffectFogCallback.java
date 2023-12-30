package io.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface ShouldApplyStatusEffectFogCallback {
    Event<ShouldApplyStatusEffectFogCallback> EVENT = EventFactory.createArrayBacked(
            ShouldApplyStatusEffectFogCallback.class, (cameraEntity, effect, callback) -> {},
            listeners -> (cameraEntity, effect, callback) -> {
                for (ShouldApplyStatusEffectFogCallback listener : listeners) {
                    listener.shouldApplyStatusEffectFog(cameraEntity, effect, callback);

                    if (callback.isCancelled()) {
                        return;
                    }
                }
            });

    void shouldApplyStatusEffectFog(LivingEntity cameraEntity, StatusEffect effect,
                                    CallbackInfoReturnable<Boolean> callback);
}
