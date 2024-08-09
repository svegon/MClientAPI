package io.github.svegon.mclientapi.mixininterface

interface IPlayerMoveC2SPacket {
    var x: Double

    var y: Double

    var z: Double

    var yaw: Float

    var pitch: Float

    var onGround: Boolean
}
