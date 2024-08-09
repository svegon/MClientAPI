package io.github.svegon.mclientapi.util

import net.minecraft.client.render.BackgroundRenderer
import net.minecraft.client.render.FogShape

class FogContext(private val fogData: BackgroundRenderer.FogData) {
    val fogType: BackgroundRenderer.FogType
        get() {
            return fogData.fogType
        }

    val fogStart: Float
        get() {
            return fogData.fogStart
        }

    val fogEnd: Float
        get() {
            return fogData.fogEnd
        }

    var fogShape: FogShape
        get() {
            return fogData.fogShape
        }
        set(shape) {
            fogData.fogShape = shape
        }
}
