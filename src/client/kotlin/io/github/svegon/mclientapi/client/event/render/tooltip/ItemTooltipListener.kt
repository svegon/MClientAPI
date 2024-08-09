package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface ItemTooltipListener {
    fun addItemTooltip(stack: ItemStack, context: TooltipContext, player: PlayerEntity?, type: TooltipType,
                       callback: CallbackInfoReturnable<List<Text>>, tooltip: List<Text>)

    companion object {
        @JvmField
        val EVENT: Event<ItemTooltipListener> = EventFactory.createArrayBacked(
            ItemTooltipListener::class.java,
            ItemTooltipListener { stack: ItemStack, context: TooltipContext, player: PlayerEntity?, type: TooltipType,
                                  callback: CallbackInfoReturnable<List<Text>>, tooltip: List<Text> -> }
        ) { listeners: Array<ItemTooltipListener> ->
            ItemTooltipListener { stack: ItemStack, context: TooltipContext, player: PlayerEntity?, type: TooltipType,
                                  callback: CallbackInfoReturnable<List<Text>>, tooltip: List<Text> ->
                for (listener in listeners) {
                    listener.addItemTooltip(stack, context, player, type, callback, tooltip)

                    if (callback.isCancelled) {
                        return@ItemTooltipListener
                    }
                }
            }
        }
    }
}
