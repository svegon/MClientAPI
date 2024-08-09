package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.FogModifierCallback;
import io.github.svegon.mclientapi.client.mixinterface.IStatusEffectFogModifier;
import io.github.svegon.mclientapi.client.util.FogContext;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @Inject(method = "getFogModifier", at = @At("HEAD"), cancellable = true)
    private static void onGetFogModifier(Entity entity, float tickDelta,
                                         CallbackInfoReturnable<BackgroundRenderer.StatusEffectFogModifier> callback) {
        CallbackInfoReturnable<IStatusEffectFogModifier> eventCallback = new CallbackInfoReturnable<>(callback.getId(),
                true);

        FogModifierCallback.EVENT.invoker().getFogModifier(entity, tickDelta, eventCallback);

        if (eventCallback.isCancelled()) {
            final IStatusEffectFogModifier modifier = eventCallback.getReturnValue();

            callback.setReturnValue(modifier == null ? null : new BackgroundRenderer.StatusEffectFogModifier() {
                @Override
                public RegistryEntry<StatusEffect> getStatusEffect() {
                    return modifier.getStatusEffect();
                }

                @Override
                public void applyStartEndModifier(BackgroundRenderer.FogData fogData, LivingEntity entity,
                                                  StatusEffectInstance effect, float viewDistance, float tickDelta) {
                    modifier.applyStartEndModifier(new FogContext(fogData), entity, effect, viewDistance, tickDelta);
                }

                @Override
                public boolean shouldApply(LivingEntity entity, float tickDelta) {
                    return modifier.shouldApply(entity, tickDelta);
                }

                @Override
                public float applyColorModifier(LivingEntity entity, StatusEffectInstance effect, float f,
                                                float tickDelta) {
                    return modifier.applyColorModifier(entity, effect, f, tickDelta);
                }
            });
        }
    }
}
