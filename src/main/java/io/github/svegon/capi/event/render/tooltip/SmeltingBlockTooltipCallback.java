package io.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public interface SmeltingBlockTooltipCallback {
    Event<SmeltingBlockTooltipCallback> EVENT = EventFactory.createArrayBacked(SmeltingBlockTooltipCallback.class,
            (block,stack, world, tooltip, options) -> {}, listeners -> (block,stack, world, tooltip, context) -> {
        for (SmeltingBlockTooltipCallback listener : listeners) {
            listener.appendSmeltingBlockTooltip(block, stack, world, tooltip, context);
        }
    });

    void appendSmeltingBlockTooltip(AbstractFurnaceBlock block, ItemStack stack, @Nullable BlockView world,
                                    List<Text> tooltip, TooltipContext options);
}
