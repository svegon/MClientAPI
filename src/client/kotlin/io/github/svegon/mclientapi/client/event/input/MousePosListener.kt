package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import java.util.function.Function
import kotlin.Array
import kotlin.Double
import kotlin.invoke

fun interface MousePosListener {
    fun onMousePos(prevX: Double, prevY: Double, x: Double, y: Double, deltaX: Double, deltaY: Double)

    companion object {
        @JvmField
        val EVENT: Event<MousePosListener> = EventFactory.createArrayBacked(
            MousePosListener::class.java,
            MousePosListener { prevX: Double, prevY: Double, x: Double, y: Double, deltaX: Double, deltaY: Double -> }
        ) { listeners: Array<MousePosListener> ->
            MousePosListener { prevX: Double, prevY: Double, x: Double, y: Double, deltaX: Double, deltaY: Double ->
                for (listener in listeners) {
                    listener.onMousePos(prevX, prevY, x, y, deltaX, deltaY)
                }
            }
        }
    }
}
