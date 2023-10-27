package io.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@FunctionalInterface
public interface GetItemStackTooltipCallback {
    Event<GetItemStackTooltipCallback> EVENT = EventFactory.createArrayBacked(GetItemStackTooltipCallback.class,
            (stack, player, context, callback) -> {}, listeners -> (stack, player, context, callback) -> {
        for (GetItemStackTooltipCallback listener : listeners) {
            listener.getItemStackTooltip(stack, player, context, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void getItemStackTooltip(ItemStack stack, @Nullable PlayerEntity player, TooltipContext context,
                             CallbackInfoReturnable<List<Text>> callback);
}
