package io.github.svegon.mclientapi.client.mixinterface

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.world.entity.Entity

interface IEntityRenderer<T: Entity, S: EntityRenderState> {
    fun `mClientAPI$renderLabel`(state: S, poseStack: PoseStack, submitNodeCollector: SubmitNodeCollector,
                                 camera: CameraRenderState, offset: Int = 0)
}