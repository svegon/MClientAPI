package io.github.svegon.mclientapi.client.mixin.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.svegon.mclientapi.client.mixinterface.ILevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements ResourceManagerReloadListener, AutoCloseable, ILevelRenderer {
    @Shadow private void renderBlockOutline(final MultiBufferSource.BufferSource bufferSource,
                                            final PoseStack poseStack, final boolean onlyTranslucentBlocks,
                                            final LevelRenderState levelRenderState) { throw new AssertionError(); }

    @Override
    public void mClientAPI$drawBlockOutline(MultiBufferSource.@NotNull BufferSource bufferSource,
                                            @NotNull PoseStack poseStack, boolean onlyTranslucentBlocks,
                                            @NotNull LevelRenderState levelRenderState) {
        renderBlockOutline(bufferSource, poseStack, onlyTranslucentBlocks, levelRenderState);
    }
}
