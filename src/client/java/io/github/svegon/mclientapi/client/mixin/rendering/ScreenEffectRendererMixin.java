package io.github.svegon.mclientapi.client.mixin.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.svegon.mclientapi.client.event.render.overlay.FireOverlayRenderListener;
import io.github.svegon.mclientapi.client.event.render.overlay.InWallStateListener;
import io.github.svegon.mclientapi.client.event.render.overlay.UnderwaterOverlayRenderListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
    @Inject(method = "getViewBlockingState", at = @At("HEAD"), cancellable = true)
    private static void onGetInWallBlockState(Player player, CallbackInfoReturnable<@Nullable BlockState> cir) {
        InWallStateListener.Companion.getEVENT().invoker().onInWallStateGet(player, cir);
    }

    @Inject(method = "renderWater", at = @At("HEAD"), cancellable = true)
    private static void onRenderUnderwaterOverlay(Minecraft minecraft, PoseStack poseStack,
                                                  MultiBufferSource bufferSource, CallbackInfo ci) {
        UnderwaterOverlayRenderListener.Companion.getEVENT().invoker().onUnderwaterOverlayRender(minecraft,
                poseStack, bufferSource, ci);
    }

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private static void onRenderFireOverlay(PoseStack poseStack, MultiBufferSource bufferSource,
                                            TextureAtlasSprite sprite, CallbackInfo ci) {
        FireOverlayRenderListener.Companion.getEVENT().invoker().onFireOverlayRender(poseStack, bufferSource,
                sprite, ci);
    }
}
