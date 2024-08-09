package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.EnderChestTooltipCallback;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.function.Supplier;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin extends AbstractChestBlock<EnderChestBlockEntity> implements Waterloggable {
    private EnderChestBlockMixin(Settings settings,
                                 Supplier<BlockEntityType<? extends EnderChestBlockEntity>> blockEntityTypeSupplier) {
        super(settings, blockEntityTypeSupplier);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        EnderChestTooltipCallback.EVENT.invoker().appendEnderChestTooltip((EnderChestBlock) (Object)
                this, stack, context, tooltip, options);
    }
}
