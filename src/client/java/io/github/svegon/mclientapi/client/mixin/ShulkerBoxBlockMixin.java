package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.ShulkerBoxTooltipListener;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin extends BlockWithEntity {
    private ShulkerBoxBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    public void onAppendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options,
                                CallbackInfo callback) {
        ShulkerBoxTooltipListener.EVENT.invoker().appendShulkerBoxTooltip((ShulkerBoxBlock) (Object)
                this, stack, context, tooltip, options, callback);
    }
}
