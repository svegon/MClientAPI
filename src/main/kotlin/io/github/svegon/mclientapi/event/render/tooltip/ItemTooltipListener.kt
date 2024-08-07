package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import java.util.function.Function

interface ItemTooltipListener {
    fun addItemTooltip(stack: ItemStack?, player: PlayerEntity?, context: TooltipContext?, tooltip: List<Text?>?)

    companion object {
        @JvmField
        val EVENT: Event<ItemTooltipListener?> = EventFactory.createArrayBacked<ItemTooltipListener?>(
            ItemTooltipListener::class.java,
            ItemTooltipListener { stack: ItemStack?, player: PlayerEntity?, context: TooltipContext?, tooltip: List<Text?>? -> },
            Function<Array<ItemTooltipListener?>, ItemTooltipListener?> { listeners: Array<ItemTooltipListener?>? ->
                ItemTooltipListener { stack: ItemStack?, player: PlayerEntity?, context: TooltipContext?, tooltip: List<Text?>? ->
                    for (listener in listeners) {
                        listener.addItemTooltip(stack, player, context, tooltip)
                    }
                }
            })
    }
}
