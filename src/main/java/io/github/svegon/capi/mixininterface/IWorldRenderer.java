package io.github.svegon.capi.mixininterface;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

public interface IWorldRenderer {
    void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Camera camera, BlockPos pos);

    void drawBlockOutline(MatrixStack matrices, Camera camera, BlockPos pos);
}
