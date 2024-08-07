package io.github.svegon.capi.mixininterface

import net.minecraft.client.render.Camera

interface IWorldRenderer {
    fun drawBlockOutline(matrices: MatrixStack?, vertexConsumer: VertexConsumer?, camera: Camera?, pos: BlockPos?)

    fun drawBlockOutline(matrices: MatrixStack?, camera: Camera?, pos: BlockPos?)
}
