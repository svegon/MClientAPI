package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.render.tooltip.BrewingStandTooltipCallback;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(BrewingStandBlock.class)
public abstract class BrewingStandBlockMixin extends BlockWithEntity {
    private BrewingStandBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        BrewingStandTooltipCallback.EVENT.invoker().appendBrewingStandTooltip((BrewingStandBlock) (Object) this,
                stack, world, tooltip, options);
    }
}
