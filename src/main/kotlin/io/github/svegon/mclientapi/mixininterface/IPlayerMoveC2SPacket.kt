package io.github.svegon.capi.mixininterface

interface IPlayerMoveC2SPacket {
    var x: Double

    var y: Double

    var z: Double

    var yaw: Float

    var pitch: Float

    fun setOnGround(onGround: Boolean)
}
