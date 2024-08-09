package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.ChestTooltipCallback;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin extends AbstractChestBlock<ChestBlockEntity> implements Waterloggable {
    private ChestBlockMixin(Settings settings,
                            Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityTypeSupplier) {
        super(settings, blockEntityTypeSupplier);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        ChestTooltipCallback.EVENT.invoker().appendChestTooltip((ChestBlock) (Object) this, stack, context, tooltip,
                options);
    }
}
