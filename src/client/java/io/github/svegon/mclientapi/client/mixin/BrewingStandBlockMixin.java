package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.BrewingStandTooltipCallback;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(BrewingStandBlock.class)
public abstract class BrewingStandBlockMixin extends BlockWithEntity {
    private BrewingStandBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        BrewingStandTooltipCallback.EVENT.invoker().appendBrewingStandTooltip((BrewingStandBlock) (Object)
                this, stack, context, tooltip, options);
    }
}
