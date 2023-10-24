package com.github.svegon.capi.mixininterface;

import com.github.svegon.capi.util.FogContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

public interface IStatusEffectFogModifier {
    StatusEffect getStatusEffect();

    void applyStartEndModifier(FogContext fogData, LivingEntity entity,
                               StatusEffectInstance effect, float viewDistance, float tickDelta);

    default boolean shouldApply(LivingEntity entity, float tickDelta) {
        return entity.hasStatusEffect(this.getStatusEffect());
    }

    default float applyColorModifier(LivingEntity entity, StatusEffectInstance effect, float horizonShadingRatio,
                                     float tickDelta) {
        StatusEffectInstance statusEffectInstance = entity.getStatusEffect(this.getStatusEffect());

        if (statusEffectInstance != null) {
            horizonShadingRatio = statusEffectInstance.getDuration() < 20
                    ? 1.0f - (float)statusEffectInstance.getDuration() / 20.0f : 0.0f;
        }

        return horizonShadingRatio;
    }
}
