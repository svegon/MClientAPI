package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import java.util.function.Function

interface AppendItemTooltipListener {
    fun appendItemTooltip(stack: ItemStack?, player: PlayerEntity?, context: TooltipContext?, tooltip: List<Text?>?)

    companion object {
        @JvmField
        val EVENT: Event<AppendItemTooltipListener?> = EventFactory.createArrayBacked<AppendItemTooltipListener?>(
            AppendItemTooltipListener::class.java,
            AppendItemTooltipListener { stack: ItemStack?, player: PlayerEntity?, context: TooltipContext?, tooltip: List<Text?>? -> },
            Function<Array<AppendItemTooltipListener?>, AppendItemTooltipListener?> { listeners: Array<AppendItemTooltipListener?>? ->
                AppendItemTooltipListener { stack: ItemStack?, player: PlayerEntity?, context: TooltipContext?, tooltip: List<Text?>? ->
                    for (listener in listeners) {
                        listener.appendItemTooltip(stack, player, context, tooltip)
                    }
                }
            })
    }
}
