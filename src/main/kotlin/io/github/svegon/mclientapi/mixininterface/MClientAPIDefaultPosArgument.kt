package io.github.svegon.mclientapi.mixininterface

import net.minecraft.command.argument.CoordinateArgument

interface MClientAPIDefaultPosArgument {
    val x: CoordinateArgument

    val y: CoordinateArgument

    val z: CoordinateArgument
}
