package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.IWorldCoordinates;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldCoordinates.class)
public abstract class WorldCoordinatesMixin implements Coordinates, IWorldCoordinates {
    @Shadow @Final private WorldCoordinate x;

    @Shadow @Final private WorldCoordinate y;

    @Shadow @Final private WorldCoordinate z;

    @Override
    public @NotNull WorldCoordinate getX() {
        return x;
    }

    @Override
    public @NotNull WorldCoordinate getY() {
        return y;
    }

    @Override
    public @NotNull WorldCoordinate getZ() {
        return z;
    }
}
