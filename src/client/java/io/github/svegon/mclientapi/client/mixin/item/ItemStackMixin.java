package io.github.svegon.mclientapi.client.mixin.item;

import io.github.svegon.mclientapi.client.event.render.tooltip.AppendItemTooltipListener;
import io.github.svegon.mclientapi.client.event.render.tooltip.GetItemStackTooltipListener;
import io.github.svegon.mclientapi.client.event.render.tooltip.ItemTooltipListener;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder, ItemInstance, FabricItemStack {
    @Inject(method = "getTooltipLines", at = @At("HEAD"), cancellable = true)
    private void getTooltipHEAD(Item.TooltipContext context, @Nullable Player player,
                                TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        GetItemStackTooltipListener.Companion.getEVENT().invoker().getItemStackTooltip((ItemStack) (Object)
                this, context, player, tooltipFlag, cir);
    }

    @Inject(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;" +
            "appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;" +
            "Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;" +
            "Lnet/minecraft/world/item/TooltipFlag;)V"), cancellable = true)
    private void getTooltipBEFORE_ITEM(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player,
                                       TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        ItemTooltipListener.Companion.getEVENT().invoker().addItemTooltip((ItemStack) (Object) this, context, display,
                player, tooltipFlag, (text) -> {
                    builder.accept(text);
                    return null;
                }, ci);
    }

    @Inject(method = "addDetailsToTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;" +
            "appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;" +
            "Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;" +
            "Lnet/minecraft/world/item/TooltipFlag;)V", shift = At.Shift.AFTER), cancellable = true)
    private void getTooltipAFTER_ITEM(Item.TooltipContext context, TooltipDisplay display, @Nullable Player player,
                                      TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        AppendItemTooltipListener.Companion.getEVENT().invoker().appendItemTooltip((ItemStack) (Object) this, context,
                display, player, tooltipFlag, (text) -> {
                    builder.accept(text);
                    return null;
                }, ci);
    }
}
