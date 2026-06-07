package io.github.svegon.mclientapi.client.event.render.tooltip

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.chat.Component
import net.minecraft.world.item.component.TypedEntityData
import net.minecraft.world.level.block.entity.BlockEntityType
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface SpawnerTooltipListener {
    fun appendSpawnerTooltip(
        data: TypedEntityData<BlockEntityType<*>>?, builder: (Component) -> Void, nextSpawnDataTagKey: String,
        ci: CallbackInfo
    )

    companion object {
        val EVENT: Event<SpawnerTooltipListener> = EventFactory.createArrayBacked(
            SpawnerTooltipListener::class.java,
            SpawnerTooltipListener { data: TypedEntityData<BlockEntityType<*>>?,
                                     builder: (Component) -> Void, nextSpawnDataTagKey: String,  ci: CallbackInfo -> }
        ) { listeners: Array<SpawnerTooltipListener> ->
            SpawnerTooltipListener { data: TypedEntityData<BlockEntityType<*>>?, builder: (Component) -> Void,
                                     nextSpawnDataTagKey: String, ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.appendSpawnerTooltip(data, builder, nextSpawnDataTagKey, ci)

                    if (ci.isCancelled) {
                        return@SpawnerTooltipListener
                    }
                }
            }
        }
    }
}
