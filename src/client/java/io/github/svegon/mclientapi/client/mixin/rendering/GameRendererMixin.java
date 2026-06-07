package io.github.svegon.mclientapi.client.mixin.rendering;

import io.github.svegon.mclientapi.client.event.render.block.ShouldRenderBlockOutlineListener;
import io.github.svegon.mclientapi.client.event.render.light.NightVisionStrengthListener;
import io.github.svegon.mclientapi.client.event.render.screen.LoadingOverlayRenderListener;
import io.github.svegon.mclientapi.client.mixinterface.IGameRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements IGameRenderer {
    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionStrength(LivingEntity camera, float a, CallbackInfoReturnable<Float> cir) {
        NightVisionStrengthListener.Companion.getEVENT().invoker().getNightVisionStrength(camera, a, cir);
    }

    @Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
    private void onShouldRenderBlockOutline(CallbackInfoReturnable<Boolean> callback) {
        ShouldRenderBlockOutlineListener.Companion.getEVENT().invoker().onShouldRenderBlockOutline(
                (GameRenderer) (Object) this, callback);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Renderable;" +
            "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", ordinal = 0),
            target = @Desc(owner = Renderable.class, value = "extractRenderState", args = {GuiGraphicsExtractor.class,
                    int.class, int.class, float.class}))
    private void renderOverlay(Renderable instance, final GuiGraphicsExtractor graphics, int mouseX, int mouseY,
                               final float a) {
        CallbackInfo callback = new CallbackInfo("Lnet/minecraft/client/gui/components/Renderable;" +
                "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", true);

        LoadingOverlayRenderListener.Companion.getEVENT().invoker().onLoadingOverlayRender((Overlay) instance, graphics,
                mouseX, mouseY, a, callback);

        if (!callback.isCancelled()) {
            instance.extractRenderState(graphics, mouseX, mouseY, a);
        }
    }
}
