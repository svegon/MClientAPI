package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.BarrelTooltipCallback;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(BarrelBlock.class)
public abstract class BarrelBlockMixin extends BlockWithEntity {
    private BarrelBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        BarrelTooltipCallback.EVENT.invoker().appendBarrelTooltip((BarrelBlock) (Object) this,
                stack, context, tooltip, options);
    }
}
