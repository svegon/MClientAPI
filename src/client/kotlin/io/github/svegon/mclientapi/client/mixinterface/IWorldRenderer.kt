package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos

interface IWorldRenderer {
    fun `mClientAPI$drawBlockOutline`(matrices: MatrixStack, vertexConsumer: VertexConsumer, camera: Camera, pos: BlockPos)
}
