package io.github.svegon.mclientapi.client.mixinterface

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator

interface IRenderTickCounter {
    var tickDeltaBeforePause: Float
    var timeMillis: Long
    var targetMillisPerTick: FloatUnaryOperator
    var paused: Boolean
    var tickFrozen: Boolean
    var prevTimeMillis: Long
    var tickTime: Float

    var tpS: Float
        get() = 1000 / tickTime
        set(tps) {
            tickTime = 1000 / tps
        }
}
