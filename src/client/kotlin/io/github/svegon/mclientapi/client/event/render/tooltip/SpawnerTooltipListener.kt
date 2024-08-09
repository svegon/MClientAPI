package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.block.SpawnerBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface SpawnerTooltipListener {
    fun appendSpawnerTooltip(
        block: SpawnerBlock, stack: ItemStack, context: Item.TooltipContext,
        tooltip: List<Text>, options: TooltipType, callback: CallbackInfo
    )

    companion object {
        @JvmField
        val EVENT: Event<SpawnerTooltipListener> = EventFactory.createArrayBacked(
            SpawnerTooltipListener::class.java,
            SpawnerTooltipListener { block: SpawnerBlock, stack: ItemStack, context: Item.TooltipContext,
                                     tooltip: List<Text>, options: TooltipType, callback: CallbackInfo -> }
        ) { listeners: Array<SpawnerTooltipListener> ->
            SpawnerTooltipListener { block: SpawnerBlock, stack: ItemStack, context: Item.TooltipContext,
                                     tooltip: List<Text>, options: TooltipType, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.appendSpawnerTooltip(block, stack, context, tooltip, options, callback)

                    if (callback.isCancelled) {
                        return@SpawnerTooltipListener
                    }
                }
            }
        }
    }
}
