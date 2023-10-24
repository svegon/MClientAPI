package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.render.BlockEntityRenderCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin implements SynchronousResourceReloader {
    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;" +
            "Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"), cancellable = true)
    private <E extends BlockEntity> void onRender(E blockEntity, float tickDelta, MatrixStack matrices,
                                                       VertexConsumerProvider vertexConsumers, CallbackInfo callback) {
        BlockEntityRenderCallback.EVENT.invoker().onBlockEntityRender(blockEntity, tickDelta, matrices,
                vertexConsumers, callback);
    }
}
