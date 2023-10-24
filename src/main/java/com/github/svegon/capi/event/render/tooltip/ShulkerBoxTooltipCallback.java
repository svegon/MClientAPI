package com.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

public interface ShulkerBoxTooltipCallback {
    Event<ShulkerBoxTooltipCallback> EVENT = EventFactory.createArrayBacked(ShulkerBoxTooltipCallback.class,
            (stack, world, tooltip, options, callback) -> {},
            listeners -> (stack, world, tooltip, context, callback) -> {
        for (ShulkerBoxTooltipCallback listener : listeners) {
            listener.appendShulkerBoxTooltip(stack, world, tooltip, context, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void appendShulkerBoxTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip,
                                 TooltipContext options, CallbackInfo callback);
}
