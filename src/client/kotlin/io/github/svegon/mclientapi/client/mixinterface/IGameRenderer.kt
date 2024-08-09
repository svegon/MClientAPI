package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.entity.Entity
import net.minecraft.util.hit.HitResult

interface IGameRenderer {
    fun `mClientAPI$raycast`(camera: Entity, blockInteractionRange: Double, entityInteractionRange: Double,
                             tickDelta: Float): HitResult
}