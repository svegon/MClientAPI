package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.util.hit.HitResult
import java.util.function.Function
import kotlin.Array

fun interface CrosshairTargetTypeCallback {
    fun getCrosshairTargetType(hitResult: HitResult): HitResult.Type

    object EmptyListener : CrosshairTargetTypeCallback {
        override fun getCrosshairTargetType(hitResult: HitResult): HitResult.Type {
            return hitResult.type
        }
    }

    object InvokerFuctory : Function<Array<CrosshairTargetTypeCallback>, CrosshairTargetTypeCallback> {
        override fun apply(listeners: Array<CrosshairTargetTypeCallback>): CrosshairTargetTypeCallback {
            return CrosshairTargetTypeCallback { hitResult: HitResult ->
                val type: HitResult.Type = hitResult.type

                for (listener in listeners) {
                    val newType: HitResult.Type = listener.getCrosshairTargetType(hitResult)

                    if (newType != type) {
                        return@CrosshairTargetTypeCallback newType
                    }
                }
                type
            }
        }
    }

    companion object {
        @JvmField
        val BLOCK_BREAK: Event<CrosshairTargetTypeCallback> = EventFactory.createArrayBacked(
            CrosshairTargetTypeCallback::class.java,
            EmptyListener, InvokerFuctory
        )
        @JvmField
        val ATTACK: Event<CrosshairTargetTypeCallback> = EventFactory.createArrayBacked(
            CrosshairTargetTypeCallback::class.java,
            EmptyListener, InvokerFuctory
        )
        @JvmField
        val ITEM_USE: Event<CrosshairTargetTypeCallback> = EventFactory.createArrayBacked(
            CrosshairTargetTypeCallback::class.java,
            EmptyListener, InvokerFuctory
        )
        @JvmField
        val ITEM_PICK: Event<CrosshairTargetTypeCallback> = EventFactory.createArrayBacked(
            CrosshairTargetTypeCallback::class.java,
            EmptyListener, InvokerFuctory
        )
    }
}
