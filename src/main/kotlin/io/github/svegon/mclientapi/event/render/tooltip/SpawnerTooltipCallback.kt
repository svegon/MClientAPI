package io.github.svegon.capi.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text
import net.minecraft.world.BlockView

fun interface SpawnerTooltipCallback {
    fun appendSpawnerTooltip(stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext?)

    companion object {
        @JvmField
        val EVENT: Event<SpawnerTooltipCallback> = EventFactory.createArrayBacked<SpawnerTooltipCallback>(
            SpawnerTooltipCallback::class.java,
            SpawnerTooltipCallback { stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, options: TooltipContext? -> }) { listeners: Array<SpawnerTooltipCallback> ->
            SpawnerTooltipCallback { stack: ItemStack?, world: BlockView?, tooltip: List<Text?>?, context: TooltipContext? ->
                for (listener in listeners) {
                    listener.appendSpawnerTooltip(stack, world, tooltip, context)
                }
            }
        }
    }
}
