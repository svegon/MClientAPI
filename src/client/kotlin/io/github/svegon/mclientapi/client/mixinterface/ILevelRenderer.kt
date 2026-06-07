package io.github.svegon.mclientapi.client.mixinterface

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.state.level.LevelRenderState

interface ILevelRenderer {
    fun `mClientAPI$drawBlockOutline`(
        bufferSource: MultiBufferSource.BufferSource, poseStack: PoseStack,
                                      onlyTranslucentBlocks: Boolean, levelRenderState: LevelRenderState
    )
}
