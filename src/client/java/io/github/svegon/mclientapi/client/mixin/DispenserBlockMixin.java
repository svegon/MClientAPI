package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.DispenserOrDropperTooltipCallback;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin extends BlockWithEntity {
    private DispenserBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        DispenserOrDropperTooltipCallback.EVENT.invoker().appendDispenserOrDropperTooltip((DispenserBlock) (Object) this,
                stack, context, tooltip, options);
    }
}
