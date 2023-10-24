package com.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public interface ChestTooltipCallback {
    Event<ChestTooltipCallback> EVENT = EventFactory.createArrayBacked(ChestTooltipCallback.class,
            (block,stack, world, tooltip, options) -> {}, listeners -> (block,stack, world, tooltip, context) -> {
        for (ChestTooltipCallback listener : listeners) {
            listener.appendChestTooltip(block, stack, world, tooltip, context);
        }
    });

    void appendChestTooltip(ChestBlock block, ItemStack stack, @Nullable BlockView world, List<Text> tooltip,
                            TooltipContext options);
}
