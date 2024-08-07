package io.github.svegon.capi.mixin;

import io.github.svegon.capi.mixininterface.IDefaultPosArgument;
import net.minecraft.command.argument.CoordinateArgument;
import net.minecraft.command.argument.DefaultPosArgument;
import net.minecraft.command.argument.PosArgument;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DefaultPosArgument.class)
public abstract class DefaultPosArgumentMixin implements PosArgument, IDefaultPosArgument {
    @Shadow @Final private CoordinateArgument x;

    @Shadow @Final private CoordinateArgument y;

    @Shadow @Final private CoordinateArgument z;

    @Override
    public CoordinateArgument getX() {
        return x;
    }

    @Override
    public CoordinateArgument getY() {
        return y;
    }

    @Override
    public CoordinateArgument getZ() {
        return z;
    }
}
