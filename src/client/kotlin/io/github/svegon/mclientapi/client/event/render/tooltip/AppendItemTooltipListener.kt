package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface AppendItemTooltipListener {
    fun appendItemTooltip(stack: ItemStack, context: Item.TooltipContext, display: TooltipDisplay, player: Player?,
                          tooltipFlag: TooltipFlag, builder: (Component) -> Void, ci: CallbackInfo)

    companion object {
        val EVENT: Event<AppendItemTooltipListener> = EventFactory.createArrayBacked(
            AppendItemTooltipListener::class.java,
            AppendItemTooltipListener { stack: ItemStack, context: Item.TooltipContext,
                                        display: TooltipDisplay, player: Player?, tooltipFlag: TooltipFlag,
                                        builder: (Component) -> Void, ci: CallbackInfo -> }
        ) { listeners: Array<AppendItemTooltipListener> ->
            AppendItemTooltipListener { stack: ItemStack, context: Item.TooltipContext, display: TooltipDisplay,
                                        player: Player?,  tooltipFlag: TooltipFlag, builder: (Component) -> Void,
                                        ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.appendItemTooltip(stack, context, display, player, tooltipFlag, builder, ci)

                    if (ci.isCancelled) {
                        return@AppendItemTooltipListener
                    }
                }
            }
        }
    }
}
