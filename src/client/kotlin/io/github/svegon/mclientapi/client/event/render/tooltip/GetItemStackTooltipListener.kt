package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface GetItemStackTooltipListener {
    fun getItemStackTooltip(
        stack: ItemStack, context: TooltipContext, player: PlayerEntity?, type: TooltipType,
        callback: CallbackInfoReturnable<List<Text>>
    )

    companion object {
        @JvmField
        val EVENT: Event<GetItemStackTooltipListener> = EventFactory.createArrayBacked(
            GetItemStackTooltipListener::class.java,
            GetItemStackTooltipListener { stack: ItemStack, context: TooltipContext, player: PlayerEntity?,
                                          type: TooltipType, callback: CallbackInfoReturnable<List<Text>> -> }
        ) { listeners: Array<GetItemStackTooltipListener> ->
            GetItemStackTooltipListener { stack: ItemStack, context: TooltipContext, player: PlayerEntity?,
                                          type: TooltipType, callback: CallbackInfoReturnable<List<Text>> ->
                for (listener in listeners) {
                    listener.getItemStackTooltip(stack, context, player, type, callback)

                    if (callback.isCancelled) {
                        return@GetItemStackTooltipListener
                    }
                }
            }
        }
    }
}
