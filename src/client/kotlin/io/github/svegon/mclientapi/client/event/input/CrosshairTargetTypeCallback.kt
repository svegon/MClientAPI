package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.Minecraft
import net.minecraft.world.phys.HitResult
import java.util.function.Function
import kotlin.Array
import kotlin.math.min

fun interface CrosshairTargetTypeCallback {
    fun getCrosshairTargetType(minecraft: Minecraft, hitResult: HitResult): HitResult.Type

    object EmptyListener : CrosshairTargetTypeCallback {
        override fun getCrosshairTargetType(minecraft: Minecraft, hitResult: HitResult): HitResult.Type {
            return hitResult.type
        }
    }

    object InvokerFactory : Function<Array<CrosshairTargetTypeCallback>, CrosshairTargetTypeCallback> {
        override fun apply(listeners: Array<CrosshairTargetTypeCallback>): CrosshairTargetTypeCallback {
            return CrosshairTargetTypeCallback { minecraft: Minecraft, hitResult: HitResult ->
                val type: HitResult.Type = hitResult.type

                for (listener in listeners) {
                    val newType: HitResult.Type = listener.getCrosshairTargetType(minecraft, hitResult)

                    if (newType != type) {
                        return@CrosshairTargetTypeCallback newType
                    }
                }
                type
            }
        }
    }

    companion object {
        val ITEM_USE: Event<CrosshairTargetTypeCallback> = EventFactory.createArrayBacked(
            CrosshairTargetTypeCallback::class.java,
            EmptyListener, InvokerFactory
        )
    }
}
