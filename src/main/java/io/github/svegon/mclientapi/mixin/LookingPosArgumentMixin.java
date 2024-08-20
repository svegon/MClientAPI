package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.MClientAPILookingPosArgument;
import net.minecraft.command.argument.LookingPosArgument;
import net.minecraft.command.argument.PosArgument;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LookingPosArgument.class)
public abstract class LookingPosArgumentMixin implements PosArgument, MClientAPILookingPosArgument {
    @Shadow @Final private double x;

    @Shadow @Final private double y;

    @Shadow @Final private double z;

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }
}
