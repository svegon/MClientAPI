package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.KeyboardHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.input.KeyEvent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function
import kotlin.Array
import kotlin.Int
import kotlin.invoke

fun interface KeyListener {
    fun onKeyPress(minecraft: Minecraft, action: @KeyEvent.Action Int, event: KeyEvent, ci: CallbackInfo)

    companion object {
        val EVENT: Event<KeyListener> = EventFactory.createArrayBacked(KeyListener::class.java,
            KeyListener { minecraft: Minecraft, action: @KeyEvent.Action Int, event: KeyEvent,
                          ci: CallbackInfo -> }
        ) { listeners: Array<KeyListener> -> (KeyListener { minecraft: Minecraft, action: @KeyEvent.Action Int,
                                                            event: KeyEvent, ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.onKeyPress(minecraft, action, event, ci)

                    if (ci.isCancelled) {
                        return@KeyListener
                    }
                }
            })
        }
    }
}
