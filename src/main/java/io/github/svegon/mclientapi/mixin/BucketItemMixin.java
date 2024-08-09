package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.IBucketItem;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin extends Item implements FluidModificationItem, IBucketItem {
    @Shadow @Final private Fluid fluid;

    private BucketItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public @NotNull Fluid getFluid() {
        return fluid;
    }
}
