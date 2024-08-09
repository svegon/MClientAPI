package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.CommandBlockTooltipCallback;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CommandBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin extends BlockWithEntity {
    private CommandBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        CommandBlockTooltipCallback.EVENT.invoker().appendCommandBlockTooltip((CommandBlock) (Object) this, stack,
                context, tooltip, options);
    }
}
