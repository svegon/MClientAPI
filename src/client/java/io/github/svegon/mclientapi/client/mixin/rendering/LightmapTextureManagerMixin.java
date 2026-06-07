package io.github.svegon.mclientapi.client.mixin.rendering;

import io.github.svegon.mclientapi.client.event.render.light.LightmapDarknessFactorListener;
import io.github.svegon.mclientapi.client.event.render.light.StatusEffectLightCallback;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapRenderStateExtractor.class)
public abstract class LightmapTextureManagerMixin implements AutoCloseable {
    @Inject(method = "calculateDarknessScale", at = @At("HEAD"), cancellable = true)
    private void onGetDarknessFactor(LivingEntity camera, float darknessGamma, float partialTickTime,
                                     CallbackInfoReturnable<Float> cir) {
        LightmapDarknessFactorListener.Companion.getEVENT().invoker().getDarknessFactor((LightmapRenderStateExtractor)
                (Object) this, camera,  darknessGamma, partialTickTime, cir);
    }

    @Redirect(method = "extract", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/core/Holder;)Z"),
            target = @Desc(owner = LivingEntity.class, value = "hasEffect", args = Holder.class, ret = boolean.class),
            expect = 2)
    private static boolean hasStatusEffectForUpdate(LocalPlayer instance, Holder<@NotNull MobEffect> holder) {
        return StatusEffectLightCallback.Companion.getEVENT().invoker().onLightingEffectCheck(instance, holder,
                instance.hasEffect(holder));
    }
}
