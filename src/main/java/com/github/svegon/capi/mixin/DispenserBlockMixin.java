package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.render.tooltip.DispenserOrDropperTooltipCallback;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin extends BlockWithEntity {
    private DispenserBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        DispenserOrDropperTooltipCallback.EVENT.invoker().appendDispenserOrDropperTooltip((DispenserBlock) (Object) this,
                stack, world, tooltip, options);
    }
}
