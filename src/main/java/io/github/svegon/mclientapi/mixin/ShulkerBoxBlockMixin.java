package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.render.tooltip.ShulkerBoxTooltipCallback;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
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
    public void onAppendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options,
                                CallbackInfo callback) {
        ShulkerBoxTooltipCallback.EVENT.invoker().appendShulkerBoxTooltip(stack, world, tooltip, options, callback);
    }
}
