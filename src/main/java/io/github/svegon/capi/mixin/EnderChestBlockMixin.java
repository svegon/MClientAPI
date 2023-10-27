package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.render.tooltip.EnderChestTooltipCallback;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
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
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        EnderChestTooltipCallback.EVENT.invoker().appendEnderChestTooltip(stack, world, tooltip, options);
    }
}
