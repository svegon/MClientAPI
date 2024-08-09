package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.util.math.MatrixStack
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface UnderwaterOverlayRenderListener {
    fun onUnderwaterOverlayRender(matrices: MatrixStack, callback: CallbackInfo)

    companion object {
        @JvmField
        val EVENT: Event<UnderwaterOverlayRenderListener> = EventFactory.createArrayBacked(
            UnderwaterOverlayRenderListener::class.java,
                UnderwaterOverlayRenderListener { matrices: MatrixStack, callback: CallbackInfo -> }
        ) { listeners: Array<UnderwaterOverlayRenderListener> ->
            UnderwaterOverlayRenderListener { matrices: MatrixStack, callback: CallbackInfo ->
                for (listener in listeners) {
                    listener.onUnderwaterOverlayRender(matrices, callback)

                    if (callback.isCancelled) {
                        return@UnderwaterOverlayRenderListener
                    }
                }
            }
        }
    }
}
