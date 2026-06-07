package io.github.svegon.mclientapi.client.mixin.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.svegon.mclientapi.client.event.render.block.BlockEntityRenderCallback;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin implements ResourceManagerReloadListener {
    @Inject(method = "submit", at = @At("HEAD"), cancellable = true)
    private <S extends BlockEntityRenderState> void onRender(S state, PoseStack poseStack,
                                                             SubmitNodeCollector submitNodeCollector,
                                                             CameraRenderState camera, CallbackInfo ci) {
        BlockEntityRenderCallback.Companion.getEVENT().invoker().onBlockEntityRender((BlockEntityRenderDispatcher)
                (Object) this, state, poseStack, submitNodeCollector, camera, ci);
    }
}
