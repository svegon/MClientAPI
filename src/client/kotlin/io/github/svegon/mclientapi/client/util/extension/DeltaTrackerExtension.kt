package io.github.svegon.mclientapi.client.util.extension

import io.github.svegon.mclientapi.client.mixinterface.ITickTimer
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator
import net.minecraft.client.DeltaTracker

object DeltaTrackerExtension {
    var DeltaTracker.Timer.tickTimeOverride: (Float) -> Float
        get() = (this as ITickTimer).`mClientAPI$targetMillisPerTick`::apply
        set(value) {
            (this as ITickTimer).`mClientAPI$targetMillisPerTick` = FloatUnaryOperator { f: Float -> value(f) }
        }

    var DeltaTracker.Timer.paused: Boolean
        get() = (this as ITickTimer).`mClientAPI$paused`
        set(value) {
            updatePauseState(value)
        }

    var DeltaTracker.Timer.frozen: Boolean
        get() = (this as ITickTimer).`mClientAPI$frozen`
        set(value) {
            updateFrozenState(value)
        }

    var DeltaTracker.Timer.prevGameTimeMs: Long
        get() = (this as ITickTimer).`mClientAPI$prevGameTime`
        set(value) {
            (this as ITickTimer).`mClientAPI$prevGameTime` = value
        }

    var DeltaTracker.Timer.prevRealTimeMs: Long
        get() = (this as ITickTimer).`mClientAPI$prevRealTime`
        set(value) {
            (this as ITickTimer).`mClientAPI$prevRealTime` = value
        }

    var DeltaTracker.Timer.tickTimeMs: Float
        get() = (this as ITickTimer).`mClientAPI$tickTime`
        set(value) {
            (this as ITickTimer).`mClientAPI$tickTime` = value
        }

    var DeltaTracker.Timer.tps: Float
        get() = 1000 / tickTimeMs
        set(value) {
            tickTimeMs = 1000 / value
        }
}