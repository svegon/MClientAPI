package io.github.svegon.mclientapi.mixininterface

import net.minecraft.commands.arguments.coordinates.WorldCoordinate

interface IWorldCoordinates {
    val x: WorldCoordinate

    val y: WorldCoordinate

    val z: WorldCoordinate
}
