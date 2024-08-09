package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.FireOverlayRenderListener;
import io.github.svegon.mclientapi.client.event.render.InWallStateListener;
import io.github.svegon.mclientapi.client.event.render.UnderwaterOverlayRenderListener;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {
    @Inject(method = "getInWallBlockState", at = @At("HEAD"), cancellable = true)
    private static void onGetInWallBlockState(PlayerEntity player,
                                              CallbackInfoReturnable<@Nullable BlockState> callback) {
        InWallStateListener.EVENT.invoker().onInWallStateGet(player, callback);
    }

    @Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
    private static void onRenderUnderwaterOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo callback) {
        UnderwaterOverlayRenderListener.EVENT.invoker().onUnderwaterOverlayRender(matrices, callback);
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void onRenderFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo callback) {
        FireOverlayRenderListener.EVENT.invoker().onFireOverlayRender(matrices, callback);
    }
}
