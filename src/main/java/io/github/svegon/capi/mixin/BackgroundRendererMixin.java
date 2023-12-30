package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.render.FogModifierCallback;
import io.github.svegon.capi.event.render.ShouldApplyStatusEffectFogCallback;
import io.github.svegon.capi.util.FogContext;
import io.github.svegon.capi.mixininterface.IStatusEffectFogModifier;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
                public StatusEffect getStatusEffect() {
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

    @Redirect(method = "applyFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;" +
            "hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"), target = @Desc(owner = LivingEntity.class,
            value = "hasStatusEffect", args = StatusEffect.class, ret = boolean.class))
    private static boolean hasStatusEffectForFog(LivingEntity entity, StatusEffect effect) {
        CallbackInfoReturnable<Boolean> callback = new CallbackInfoReturnable<>("hasStatusEffectForFog",
                true, entity.hasStatusEffect(effect));

        ShouldApplyStatusEffectFogCallback.EVENT.invoker().shouldApplyStatusEffectFog(entity, effect, callback);

        return callback.getReturnValueZ();
    }
}
