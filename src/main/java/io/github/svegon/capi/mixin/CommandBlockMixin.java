package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.render.tooltip.CommandBlockTooltipCallback;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CommandBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin extends BlockWithEntity {
    private CommandBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        CommandBlockTooltipCallback.EVENT.invoker().appendCommandBlockTooltip((CommandBlock) (Object) this,
                stack, world, tooltip, options);
    }
}
