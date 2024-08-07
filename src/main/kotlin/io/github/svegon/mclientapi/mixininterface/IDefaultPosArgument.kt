package io.github.svegon.capi.mixininterface

import net.minecraft.command.argument.CoordinateArgument

interface IDefaultPosArgument {
    val x: CoordinateArgument?

    val y: CoordinateArgument?

    val z: CoordinateArgument?
}
