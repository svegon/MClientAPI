package io.github.svegon.capi.util

import com.google.common.base.Preconditions

class FogContext(fogData: FogData) {
    private val fogData: FogData = fogData

    fun fogType(): FogType {
        return fogData.fogType
    }

    fun fogStart(): Float {
        return fogData.fogStart
    }

    fun fogStart(start: Float) {
        fogData.fogStart = start
    }

    fun fogEnd(): Float {
        return fogData.fogEnd
    }

    fun fogEnd(end: Float) {
        fogData.fogEnd = end
    }

    fun fogShape(): FogShape {
        return fogData.fogShape
    }

    fun fogShape(shape: FogShape?) {
        fogData.fogShape = Preconditions.checkNotNull<Any>(shape)
    }
}
