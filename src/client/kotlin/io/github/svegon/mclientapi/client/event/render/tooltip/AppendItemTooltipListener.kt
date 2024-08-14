package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface AppendItemTooltipListener {
    fun appendItemTooltip(stack: ItemStack, context: TooltipContext, player: PlayerEntity?, type: TooltipType,
                       callback: CallbackInfoReturnable<List<Text>>, tooltip: MutableList<Text>)

    companion object {
        @JvmField
        val EVENT: Event<AppendItemTooltipListener> = EventFactory.createArrayBacked(
            AppendItemTooltipListener::class.java,
            AppendItemTooltipListener { stack: ItemStack, context: TooltipContext, player: PlayerEntity?, type,
                                        callback: CallbackInfoReturnable<List<Text>>, tooltip -> }
        ) { listeners: Array<AppendItemTooltipListener> ->
            AppendItemTooltipListener { stack: ItemStack, context: TooltipContext, player: PlayerEntity?, type: TooltipType,
                                        callback: CallbackInfoReturnable<List<Text>>, tooltip ->
                for (listener in listeners) {
                    listener.appendItemTooltip(stack, context, player, type, callback, tooltip)

                    if (callback.isCancelled) {
                        return@AppendItemTooltipListener
                    }
                }
            }
        }
    }
}
