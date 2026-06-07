package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface GetItemStackTooltipListener {
    fun getItemStackTooltip(
        stack: ItemStack, context: Item.TooltipContext, player: Player?, flag: TooltipFlag,
        cir: CallbackInfoReturnable<List<Component>>
    )

    companion object {
        val EVENT: Event<GetItemStackTooltipListener> = EventFactory.createArrayBacked(
            GetItemStackTooltipListener::class.java,
            GetItemStackTooltipListener { stack: ItemStack, context: Item.TooltipContext, player: Player?,
                                          flag: TooltipFlag, callback: CallbackInfoReturnable<List<Component>> -> }
        ) { listeners: Array<GetItemStackTooltipListener> ->
            GetItemStackTooltipListener { stack: ItemStack, context: Item.TooltipContext, player: Player?,
                                          flag: TooltipFlag, callback: CallbackInfoReturnable<List<Component>> ->
                for (listener in listeners) {
                    listener.getItemStackTooltip(stack, context, player, flag, callback)

                    if (callback.isCancelled) {
                        return@GetItemStackTooltipListener
                    }
                }
            }
        }
    }
}
