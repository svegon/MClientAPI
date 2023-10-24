package com.github.svegon.capi.mixin;

import com.github.svegon.capi.event.render.tooltip.AppendItemTooltipListener;
import com.github.svegon.capi.event.render.tooltip.GetItemStackTooltipCallback;
import com.github.svegon.capi.event.render.tooltip.ItemTooltipListener;
import com.github.svegon.capi.event.render.tooltip.ShowTooltipSectionCallback;
import com.github.svegon.capi.mixininterface.IItemStack;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IItemStack {
    @Shadow protected abstract int getHideFlags();

    @Inject(method = "isSectionVisible", at = @At("HEAD"), cancellable = true)
    private static void onIsSectionVisible(int flags, ItemStack.TooltipSection tooltipSection,
                                           CallbackInfoReturnable<Boolean> callback) {
        ShowTooltipSectionCallback.EVENT.invoker().shouldShowTooltipSection(flags, tooltipSection, callback);
    }

    @Inject(method = "getTooltip", at = @At("HEAD"), cancellable = true)
    private void getTooltipHEAD(@Nullable PlayerEntity player, TooltipContext context,
                                CallbackInfoReturnable<List<Text>> callback) {
        GetItemStackTooltipCallback.EVENT.invoker().getItemStackTooltip((ItemStack) (Object) this, player, context,
                callback);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;" +
            "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;" +
            "Lnet/minecraft/client/item/TooltipContext;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void getTooltipBEFORE_ITEM(@Nullable PlayerEntity player, TooltipContext context,
                                       CallbackInfoReturnable<List<Text>> callback, List<Text> list) {
        ItemTooltipListener.EVENT.invoker().addItemTooltip((ItemStack) (Object) this, player, context, list);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;" +
            "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;" +
            "Lnet/minecraft/client/item/TooltipContext;)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void getTooltipAFTER_ITEM(@Nullable PlayerEntity player, TooltipContext context,
                                       CallbackInfoReturnable<List<Text>> callback, List<Text> list) {
        AppendItemTooltipListener.EVENT.invoker().appendItemTooltip((ItemStack) (Object) this, player, context, list);
    }

    @Override
    public int tooltipHideFlags() {
        return getHideFlags();
    }
}
