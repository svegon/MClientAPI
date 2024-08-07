package io.github.svegon.capi.event.input

import io.github.svegon.capi.event.input.CrosshairTargetTypeCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.util.hit.HitResult
import java.util.function.Function
import kotlin.Array
import kotlin.invoke

interface CrosshairTargetTypeCallback {
    fun getCrosshairTargetType(hitResult: HitResult?): HitResult.Type?

    companion object {
        val EMPTY_LISTENER: CrosshairTargetTypeCallback = CrosshairTargetTypeCallback { obj: HitResult -> obj.type }
        val INVOKER_FACTORY: Function<Array<CrosshairTargetTypeCallback?>, CrosshairTargetTypeCallback?> =
            Function { listeners: Array<CrosshairTargetTypeCallback?>? ->
                CrosshairTargetTypeCallback { hitResult: HitResult? ->
                    val type: HitResult.Type = hitResult.getType()
                    for (listener in listeners) {
                        val newType: HitResult.Type = listener.getCrosshairTargetType(hitResult)

                        if (newType != type) {
                            return@CrosshairTargetTypeCallback newType
                        }
                    }
                    type
                }
            }

        @JvmField
        val BLOCK_BREAK: Event<CrosshairTargetTypeCallback?> = EventFactory.createArrayBacked(
            CrosshairTargetTypeCallback::class.java,
            EMPTY_LISTENER, INVOKER_FACTORY
        )
        @JvmField
        val ATTACK: Event<CrosshairTargetTypeCallback?> = EventFactory.createArrayBacked(
            CrosshairTargetTypeCallback::class.java,
            EMPTY_LISTENER, INVOKER_FACTORY
        )
        @JvmField
        val ITEM_USE: Event<CrosshairTargetTypeCallback?> = EventFactory.createArrayBacked(
            CrosshairTargetTypeCallback::class.java,
            EMPTY_LISTENER, INVOKER_FACTORY
        )
        @JvmField
        val ITEM_PICK: Event<CrosshairTargetTypeCallback?> = EventFactory.createArrayBacked(
            CrosshairTargetTypeCallback::class.java,
            EMPTY_LISTENER, INVOKER_FACTORY
        )
    }
}
