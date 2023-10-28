package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.entity.EntityTargetUpdateCallback;
import io.github.svegon.capi.event.input.CrosshairTargetUpdateListener;
import io.github.svegon.capi.event.render.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Locale;
import java.util.function.Predicate;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow public abstract void close();

    @Mutable
    @Shadow
    @Final
    MinecraftClient client;
    @Mutable
    @Shadow
    @Final
    private LightmapTextureManager lightmapTextureManager;

    @Redirect(method = "updateTargetedEntity", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;" +
                    "Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;" +
                    "Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"))
    @Nullable
    private EntityHitResult raycastTargettedEntity(Entity entity, Vec3d min, Vec3d max, Box box,
                                                         Predicate<Entity> predicate, double d) {
        return EntityTargetUpdateCallback.EVENT.invoker().onEntityTargetUpdate(entity, min, max, box, predicate, d,
                ProjectileUtil.raycast(entity, min, max, box, predicate, d));
    }

    @Inject(method = "updateTargetedEntity", at = @At("TAIL"), cancellable = true)
    private void onUpdateTargetedEntity(float tickDelta, CallbackInfo callback) {
        CrosshairTargetUpdateListener.EVENT.invoker().onCrosshairTargetUpdate(client, tickDelta);
    }

    @Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionStrength(LivingEntity entity, float tickDelta,
                                                 CallbackInfoReturnable<Float> callback) {
        NightVisionStrengthCallback.EVENT.invoker().getNightVisionStrength(entity, tickDelta, callback);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V"),
            locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci, boolean bl,
                          int i, int j, Window window, Matrix4f matrix4f, MatrixStack matrixStack,
                          DrawContext drawContext) {
        client.getProfiler().push("modded 2D renders");

        try {
            GUIRenderListener.EVENT.invoker().onGUIRender(startTime, tick, drawContext, i, j,
                    client.getLastFrameDuration());
        } catch (Throwable t) {
            CrashReport crashReport = CrashReport.create(t, "Rendering GUI event (mods)");
            CrashReportSection crashReportSection = crashReport.addElement("GUI eventdetails");
            crashReportSection.add("Mouse location", () -> String.format(Locale.ROOT,
                    "Scaled: (%d, %d). Absolute: (%f, %f)", i, j, this.client.mouse.getX(),
                    this.client.mouse.getY()));
            crashReportSection.add("Screen size", () -> String.format(Locale.ROOT,
                    "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %f",
                    this.client.getWindow().getScaledWidth(), this.client.getWindow().getScaledHeight(),
                    this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight(),
                    this.client.getWindow().getScaleFactor()));
            throw new CrashException(crashReport);
        }
        
        client.getProfiler().pop();
    }

    @Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
    private void onShouldRenderBlockOutline(CallbackInfoReturnable<Boolean> callback) {
        ShouldRenderBlockOutlineListener.EVENT.invoker().onShouldRenderBlockOutline((GameRenderer) (Object) this,
                callback);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Overlay;" +
            "render(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    private void renderOverlay(Overlay instance, DrawContext context, int mouseX, int mouseY, float delta) {
        CallbackInfo callback = new CallbackInfo("Lnet/minecraft/client/gui/Drawable;" +
                "render(Lnet/minecraft.client/util/math/MatrixStack;IIF)V", true);

        LoadingOverlayRenderListener.EVENT.invoker().onLoadingOverlayRender(instance, context, mouseX, mouseY, delta,
                callback);

        if (!callback.isCancelled()) {
            instance.render(context, mouseX, mouseY, delta);
        }
    }

    @Inject(method = "renderFloatingItem", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD,
            target = "Lnet/minecraft/client/render/GameRenderer;floatingItemTimeLeft:I", ordinal = 1),
            cancellable = true)
    private void onRenderFloatingItem(int scaledWidth, int scaledHeight, float tickDelta, CallbackInfo callback) {
        FloatingItemRenderListener.EVENT.invoker().onFloatingItemRender(scaledWidth, scaledHeight, tickDelta, callback);
    }

    @Inject(method = "renderNausea", at = @At("HEAD"), cancellable = true)
    private void onRenderNausea(DrawContext context, float distortionStrength, CallbackInfo callback) {
        NauseaRenderCallback.EVENT.invoker().onNauseaRender((GameRenderer) (Object) this, context,
                distortionStrength, callback);
    }
}
