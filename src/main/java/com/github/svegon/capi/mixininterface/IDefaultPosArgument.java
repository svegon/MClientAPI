package com.github.svegon.capi.mixininterface;

import net.minecraft.command.argument.CoordinateArgument;

public interface IDefaultPosArgument {
    CoordinateArgument getX();

    CoordinateArgument getY();

    CoordinateArgument getZ();
}
