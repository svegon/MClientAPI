package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.HopperTooltipCallback;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HopperBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(HopperBlock.class)
public abstract class HopperBlockMixin extends BlockWithEntity {
    private HopperBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        HopperTooltipCallback.EVENT.invoker().appendHopperTooltip((HopperBlock) (Object) this,
                stack, context, tooltip, options);
    }
}
