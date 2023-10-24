package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface BlockEntityRenderCallback {
    Event<BlockEntityRenderCallback> EVENT = EventFactory.createArrayBacked(BlockEntityRenderCallback.class,
            (blockEntity, tickDelta, matrices, vertexConsumers, callback) -> {},
            listeners -> (blockEntity, tickDelta, matrices, vertexConsumers, callback) -> {
        for (BlockEntityRenderCallback listener : listeners) {
            listener.onBlockEntityRender(blockEntity, tickDelta, matrices, vertexConsumers, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void onBlockEntityRender(BlockEntity blockEntity, float tickDelta, MatrixStack matrices,
                             VertexConsumerProvider vertexConsumers, CallbackInfo callback);
}
