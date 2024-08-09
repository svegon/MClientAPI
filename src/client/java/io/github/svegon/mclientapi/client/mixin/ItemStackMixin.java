package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.AppendItemTooltipListener;
import io.github.svegon.mclientapi.client.event.render.tooltip.GetItemStackTooltipListener;
import io.github.svegon.mclientapi.client.event.render.tooltip.ItemTooltipListener;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.component.ComponentHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder, FabricItemStack {
    @Inject(method = "getTooltip", at = @At("HEAD"), cancellable = true)
    private void getTooltipHEAD(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type,
                                CallbackInfoReturnable<List<Text>> cir) {
        GetItemStackTooltipListener.EVENT.invoker().getItemStackTooltip((ItemStack) (Object)
                this, context, player, type, cir);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;appendTooltip" +
            "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/List;" +
            "Lnet/minecraft/item/tooltip/TooltipType;)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void getTooltipBEFORE_ITEM(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type,
                                       CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        ItemTooltipListener.EVENT.invoker().addItemTooltip((ItemStack) (Object) this, context, player, type, cir, list);
    }

    @Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;appendTooltip" +
            "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/List;" +
            "Lnet/minecraft/item/tooltip/TooltipType;)V", shift = At.Shift.AFTER), cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void getTooltipAFTER_ITEM(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type,
                                      CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        AppendItemTooltipListener.EVENT.invoker().appendItemTooltip((ItemStack) (Object)
                this, context, player, type, cir, list);
    }
}
