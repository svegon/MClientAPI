package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.render.OverlayRenderListener;
import io.github.svegon.capi.event.render.SpyglassOverlayRenderListener;
import io.github.svegon.capi.event.render.VignetteOverlayRenderListener;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo ci) {
        VignetteOverlayRenderListener.EVENT.invoker().onVignetteOverlayRender(context, entity, ci);
    }

    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderOverlay(DrawContext context, Identifier texture, float opacity, CallbackInfo ci) {
        OverlayRenderListener.EVENT.invoker().onOverlayRender(context, texture, opacity, ci);
    }

    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderSpyglassOverlay(DrawContext context, float scale, CallbackInfo ci) {
        SpyglassOverlayRenderListener.EVENT.invoker().onSpyglassOverlayRender(context, scale, ci);
    }
}
