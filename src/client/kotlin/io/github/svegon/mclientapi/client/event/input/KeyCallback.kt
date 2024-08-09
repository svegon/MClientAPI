package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

fun interface KeyCallback {
    fun onKeyPress(key: Int, scancode: Int, action: Int, modifiers: Int)

    companion object {
        @JvmField
        val EVENT: Event<KeyCallback> = EventFactory.createArrayBacked(
            KeyCallback::class.java,
            KeyCallback { key: Int, scancode: Int, action: Int, modifiers: Int -> }
        ) { listeners: Array<KeyCallback> ->
            (KeyCallback { key: Int, scancode: Int, action: Int, modifiers: Int ->
                for (listener in listeners) {
                    listener.onKeyPress(key, scancode, action, modifiers)
                }
            })
        }
    }
}
