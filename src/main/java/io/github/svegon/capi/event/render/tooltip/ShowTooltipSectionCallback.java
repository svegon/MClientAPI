package io.github.svegon.capi.event.render.tooltip;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@FunctionalInterface
public interface ShowTooltipSectionCallback {
    Event<ShowTooltipSectionCallback> EVENT = EventFactory.createArrayBacked(ShowTooltipSectionCallback.class,
            (flags, tooltipSection, callback) -> {}, listeners -> (flags, tooltipSection, callback) -> {
        for (ShowTooltipSectionCallback listener : listeners) {
            listener.shouldShowTooltipSection(flags, tooltipSection, callback);

            if (callback.isCancelled()) {
                return;
            }
        }
    });

    void shouldShowTooltipSection(int flags, ItemStack.TooltipSection tooltipSection,
                                  CallbackInfoReturnable<Boolean> callback);
}
