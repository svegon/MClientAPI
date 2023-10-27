package io.github.svegon.capi.event.render;

import io.github.svegon.capi.mixininterface.IStatusEffectFogModifier;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface FogModifierCallback {
    Event<FogModifierCallback> EVENT = EventFactory.createArrayBacked(FogModifierCallback.class,
            (entity, tickDelta, callback) -> {}, listeners -> (entity, tickDelta, callback) -> {
        for (FogModifierCallback listener : listeners) {
            listener.getFogModifier(entity, tickDelta, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void getFogModifier(Entity entity, float tickDelta,
                        CallbackInfoReturnable<IStatusEffectFogModifier> callback);
}
