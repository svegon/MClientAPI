package com.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AppendItemTooltipListener {
    Event<AppendItemTooltipListener> EVENT = EventFactory.createArrayBacked(AppendItemTooltipListener.class,
            (stack, player, context, tooltip) -> {}, listeners -> (stack, player, context, tooltip) -> {
                for (AppendItemTooltipListener listener : listeners) {
                    listener.appendItemTooltip(stack, player, context, tooltip);
                }
            });

    void appendItemTooltip(ItemStack stack, @Nullable PlayerEntity player, TooltipContext context, List<Text> tooltip);
}
