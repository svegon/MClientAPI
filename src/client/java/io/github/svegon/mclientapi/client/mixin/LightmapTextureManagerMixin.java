package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.LightmapDarknessFactorListener;
import io.github.svegon.mclientapi.client.event.render.StatusEffectLightCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin implements AutoCloseable {
    @Inject(method = "getDarknessFactor", at = @At("HEAD"), cancellable = true)
    private void onGetDarknessFactor(float delta, CallbackInfoReturnable<Float> callback) {
        LightmapDarknessFactorListener.EVENT.invoker().getDarknessFactor(delta, callback);
    }

    @Redirect(method = "update", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect" +
                    "(Lnet/minecraft/registry/entry/RegistryEntry;)Z"), target = @Desc(owner = LivingEntity.class,
            value = "hasStatusEffect", args = RegistryEntry.class, ret = boolean.class), expect = 2)
    private boolean hasStatusEffectForUpdate(ClientPlayerEntity instance, RegistryEntry<StatusEffect> effect) {
        return StatusEffectLightCallback.EVENT.invoker().onLightStatusEffectCheck(instance, effect,
                instance.hasStatusEffect(effect));
    }
}
