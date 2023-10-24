package com.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BarrelBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public interface BarrelTooltipCallback {
    Event<BarrelTooltipCallback> EVENT = EventFactory.createArrayBacked(BarrelTooltipCallback.class,
            (block,stack, world, tooltip, options) -> {}, listeners -> (block,stack, world, tooltip, context) -> {
        for (BarrelTooltipCallback listener : listeners) {
            listener.appendBarrelTooltip(block, stack, world, tooltip, context);
        }
    });

    void appendBarrelTooltip(BarrelBlock block, ItemStack stack, @Nullable BlockView world,
                             List<Text> tooltip, TooltipContext options);
}
