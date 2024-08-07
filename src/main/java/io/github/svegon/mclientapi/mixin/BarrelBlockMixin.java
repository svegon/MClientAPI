package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.render.tooltip.BarrelTooltipCallback;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(BarrelBlock.class)
public abstract class BarrelBlockMixin extends BlockWithEntity {
    private BarrelBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        BarrelTooltipCallback.EVENT.invoker().appendBarrelTooltip((BarrelBlock) (Object) this,
                stack, world, tooltip, options);
    }
}
