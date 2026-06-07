package io.github.svegon.mclientapi.event.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface BlockSpeedMultiplierCallback {
    fun getEntityHorizontalSpeedMultiplier(entity: Entity, cir: CallbackInfoReturnable<Float>)

    companion object {
        val EVENT: Event<BlockSpeedMultiplierCallback> = EventFactory.createArrayBacked(
            BlockSpeedMultiplierCallback::class.java,
            BlockSpeedMultiplierCallback { entity: Entity, cir: CallbackInfoReturnable<Float> -> }
        ) { listeners: Array<BlockSpeedMultiplierCallback> ->
            BlockSpeedMultiplierCallback { entity: Entity, cir: CallbackInfoReturnable<Float> ->
                for (listener in listeners) {
                    listener.getEntityHorizontalSpeedMultiplier(entity, cir)

                    if (cir.isCancelled) {
                        return@BlockSpeedMultiplierCallback
                    }
                }
            }
        }
    }
}
