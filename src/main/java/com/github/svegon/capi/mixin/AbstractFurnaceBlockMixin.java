package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.render.tooltip.SmeltingBlockTooltipCallback;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(AbstractFurnaceBlock.class)
public abstract class AbstractFurnaceBlockMixin extends BlockWithEntity {
    private AbstractFurnaceBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        SmeltingBlockTooltipCallback.EVENT.invoker().appendSmeltingBlockTooltip((AbstractFurnaceBlock) (Object) this,
                stack, world, tooltip, options);
    }
}
