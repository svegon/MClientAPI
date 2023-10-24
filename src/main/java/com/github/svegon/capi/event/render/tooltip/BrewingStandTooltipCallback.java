package com.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public interface BrewingStandTooltipCallback {
    Event<BrewingStandTooltipCallback> EVENT = EventFactory.createArrayBacked(BrewingStandTooltipCallback.class,
            (block,stack, world, tooltip, options) -> {}, listeners -> (block,stack, world, tooltip, context) -> {
        for (BrewingStandTooltipCallback listener : listeners) {
            listener.appendBrewingStandTooltip(block, stack, world, tooltip, context);
        }
    });

    void appendBrewingStandTooltip(BrewingStandBlock block, ItemStack stack, @Nullable BlockView world,
                                   List<Text> tooltip, TooltipContext options);
}
