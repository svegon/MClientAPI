package com.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public interface EnderChestTooltipCallback {
    Event<EnderChestTooltipCallback> EVENT = EventFactory.createArrayBacked(EnderChestTooltipCallback.class,
            (stack, world, tooltip, options) -> {}, listeners -> (stack, world, tooltip, context) -> {
        for (EnderChestTooltipCallback listener : listeners) {
            listener.appendEnderChestTooltip(stack, world, tooltip, context);
        }
    });

    void appendEnderChestTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options);
}
