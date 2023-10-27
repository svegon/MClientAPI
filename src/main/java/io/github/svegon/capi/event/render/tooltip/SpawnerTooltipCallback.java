package io.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public interface SpawnerTooltipCallback {
    Event<SpawnerTooltipCallback> EVENT = EventFactory.createArrayBacked(SpawnerTooltipCallback.class,
            (stack, world, tooltip, options) -> {}, listeners -> (stack, world, tooltip, context) -> {
        for (SpawnerTooltipCallback listener : listeners) {
            listener.appendSpawnerTooltip(stack, world, tooltip, context);
        }
    });

    void appendSpawnerTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options);
}
