package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.render.LightmapDarknessFactorCallback;
import io.github.svegon.capi.event.render.StatusEffectLightCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
    @Inject(method = "getDarknessFactor", at = @At("HEAD"), cancellable = true)
    private void onGetDarknessFactor(float delta, CallbackInfoReturnable<Float> callback) {
        LightmapDarknessFactorCallback.EVENT.invoker().getDarknessFactor(delta, callback);
    }

    @Redirect(method = "update", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(" +
                    "Lnet/minecraft/entity/effect/StatusEffect;)Z"), target = @Desc(owner = LivingEntity.class,
            value = "hasStatusEffect", args = StatusEffect.class, ret = boolean.class), expect = 2)
    private boolean hasStatusEffectForUpdate(ClientPlayerEntity instance, StatusEffect effect) {
        CallbackInfoReturnable<Boolean> callback = new CallbackInfoReturnable<>("hasStatusEffectForUpdate",
                true, instance.hasStatusEffect(effect));

        StatusEffectLightCallback.EVENT.invoker().onLightStatusEffectCheck(instance, effect, callback);

        return callback.getReturnValueZ();
    }
}
