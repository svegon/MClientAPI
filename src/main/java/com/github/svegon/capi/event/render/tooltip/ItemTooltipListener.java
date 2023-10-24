package com.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ItemTooltipListener {
    Event<ItemTooltipListener> EVENT = EventFactory.createArrayBacked(ItemTooltipListener.class,
            (stack, player, context, tooltip) -> {}, listeners -> (stack, player, context, tooltip) -> {
                for (ItemTooltipListener listener : listeners) {
                    listener.addItemTooltip(stack, player, context, tooltip);
                }
            });

    void addItemTooltip(ItemStack stack, @Nullable PlayerEntity player, TooltipContext context, List<Text> tooltip);
}
