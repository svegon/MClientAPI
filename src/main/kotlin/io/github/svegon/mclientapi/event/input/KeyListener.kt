package io.github.svegon.capi.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function
import kotlin.Array
import kotlin.Int
import kotlin.invoke

interface KeyListener {
    fun onKeyPress(key: Int, scancode: Int, action: Int, modifiers: Int, info: CallbackInfo?)

    companion object {
        @JvmField
        val KEY_HANDLE_START: Event<KeyListener?> = EventFactory.createArrayBacked(
            KeyListener::class.java,
            KeyListener { key: Int, scancode: Int, action: Int, modifiers: Int, info: CallbackInfo? -> },
            Function { listeners: Array<KeyListener?>? ->
                (KeyListener { key: Int, scancode: Int, action: Int, modifiers: Int, info: CallbackInfo? ->
                    for (listener in listeners) {
                        listener.onKeyPress(key, scancode, action, modifiers, info)

                        if (info.isCancelled()) {
                            return@KeyListener
                        }
                    }
                })
            })
        @JvmField
        val EVENT: Event<KeyListener?> = EventFactory.createArrayBacked(
            KeyListener::class.java,
            KeyListener { key: Int, scancode: Int, action: Int, modifiers: Int, info: CallbackInfo? -> },
            Function { listeners: Array<KeyListener?>? ->
                (KeyListener { key: Int, scancode: Int, action: Int, modifiers: Int, info: CallbackInfo? ->
                    for (listener in listeners) {
                        listener.onKeyPress(key, scancode, action, modifiers, info)
                    }
                })
            })
    }
}
