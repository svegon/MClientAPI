package io.github.svegon.mclientapi.client.event.render.screen

import com.mojang.blaze3d.buffers.GpuBufferSlice
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.render.GuiRenderer
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface GUIRenderCallback {
    fun onGUIRender(renderer: GuiRenderer, fogBuffer: GpuBufferSlice, ci: CallbackInfo)

    companion object {
        val EVENT: Event<GUIRenderCallback> = EventFactory.createArrayBacked(
            GUIRenderCallback::class.java,
            GUIRenderCallback { renderer: GuiRenderer, fogBuffer: GpuBufferSlice, ci: CallbackInfo -> }
        ) { listeners: Array<GUIRenderCallback> ->
            GUIRenderCallback { renderer: GuiRenderer, fogBuffer: GpuBufferSlice, ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.onGUIRender(renderer, fogBuffer, ci)

                    if (ci.isCancelled) {
                        return@GUIRenderCallback
                    }
                }
            }
        }
    }
}