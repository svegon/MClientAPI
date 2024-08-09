package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.SmeltingBlockTooltipCallback;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(AbstractFurnaceBlock.class)
public abstract class AbstractFurnaceBlockMixin extends BlockWithEntity {
    private AbstractFurnaceBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        SmeltingBlockTooltipCallback.EVENT.invoker().appendSmeltingBlockTooltip((AbstractFurnaceBlock) (Object) this,
                stack, context, tooltip, options);
    }
}
