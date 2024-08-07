package io.github.svegon.capi.mixininterface

interface IRenderTickCounter {
    var prevTimeMillis: Long

    var tickTime: Float

    var tpS: Float
        get() = 1000 / tickTime
        set(tps) {
            tickTime = 1000 / tps
        }
}
