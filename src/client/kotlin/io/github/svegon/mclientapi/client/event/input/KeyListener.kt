package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function
import kotlin.Array
import kotlin.Int
import kotlin.invoke

fun interface KeyListener {
    fun onKeyPress(key: Int, scancode: Int, action: Int, modifiers: Int, info: CallbackInfo)

    companion object {
        @JvmField
        val EVENT: Event<KeyListener> = EventFactory.createArrayBacked(
            KeyListener::class.java,
            KeyListener { key: Int, scancode: Int, action: Int, modifiers: Int, info: CallbackInfo -> }
        ) { listeners: Array<KeyListener> ->
            (KeyListener { key: Int, scancode: Int, action: Int, modifiers: Int, info: CallbackInfo ->
                for (listener in listeners) {
                    listener.onKeyPress(key, scancode, action, modifiers, info)

                    if (info.isCancelled) {
                        return@KeyListener
                    }
                }
            })
        }
    }
}
