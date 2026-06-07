package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.KeyboardHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.input.KeyEvent
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface KeyCallback {
    fun onKeyPress(minecraft: Minecraft, action: Int, event: KeyEvent)

    companion object {
        val EVENT: Event<KeyCallback> = EventFactory.createArrayBacked(KeyCallback::class.java,
            KeyCallback { minecraft: Minecraft, action: Int, event: KeyEvent -> }
        ) { listeners: Array<KeyCallback> ->
            (KeyCallback { minecraft: Minecraft, action: Int, event: KeyEvent ->
                for (listener in listeners) {
                    listener.onKeyPress(minecraft, action, event)
                }
            })
        }
    }
}
