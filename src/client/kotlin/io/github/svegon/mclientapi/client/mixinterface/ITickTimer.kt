package io.github.svegon.mclientapi.client.mixinterface

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator

interface ITickTimer {
    var `mClientAPI$targetMillisPerTick`: FloatUnaryOperator
    val `mClientAPI$paused`: Boolean
    var `mClientAPI$prevGameTime`: Long
    var `mClientAPI$prevRealTime`: Long
    var `mClientAPI$tickTime`: Float
    val `mClientAPI$frozen`: Boolean
}
