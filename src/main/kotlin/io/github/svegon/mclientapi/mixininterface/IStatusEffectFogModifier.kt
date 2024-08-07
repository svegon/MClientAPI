package io.github.svegon.capi.mixininterface

import io.github.svegon.capi.util.FogContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance

interface IStatusEffectFogModifier {
    @JvmField
    val statusEffect: StatusEffect?

    fun applyStartEndModifier(
        fogData: FogContext?, entity: LivingEntity?,
        effect: StatusEffectInstance?, viewDistance: Float, tickDelta: Float
    )

    fun shouldApply(entity: LivingEntity, tickDelta: Float): Boolean {
        return entity.hasStatusEffect(this.statusEffect)
    }

    fun applyColorModifier(
        entity: LivingEntity, effect: StatusEffectInstance?, horizonShadingRatio: Float,
        tickDelta: Float
    ): Float {
        var horizonShadingRatio = horizonShadingRatio
        val statusEffectInstance = entity.getStatusEffect(this.statusEffect)

        if (statusEffectInstance != null) {
            horizonShadingRatio = if (statusEffectInstance.duration < 20
            ) 1.0f - statusEffectInstance.duration.toFloat() / 20.0f else 0.0f
        }

        return horizonShadingRatio
    }
}
