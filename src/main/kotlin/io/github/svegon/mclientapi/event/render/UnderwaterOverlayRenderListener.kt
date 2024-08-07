package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.util.math.MatrixStack
import java.util.function.Function

interface UnderwaterOverlayRenderListener {
    fun onUnderwaterOverlayRender(matrices: MatrixStack?, callback: CallbackInfo?)

    companion object {
        @JvmField
        val EVENT: Event<UnderwaterOverlayRenderListener?> =
            EventFactory.createArrayBacked<UnderwaterOverlayRenderListener?>(UnderwaterOverlayRenderListener::class.java,
                UnderwaterOverlayRenderListener { matrices: MatrixStack?, callback: CallbackInfo? -> },
                Function<Array<UnderwaterOverlayRenderListener?>, UnderwaterOverlayRenderListener?> { listeners: Array<UnderwaterOverlayRenderListener?>? ->
                    UnderwaterOverlayRenderListener { matrices: MatrixStack?, callback: CallbackInfo? ->
                        for (listener in listeners) {
                            listener.onUnderwaterOverlayRender(matrices, callback)

                            if (callback.isCancelled()) {
                                return@UnderwaterOverlayRenderListener
                            }
                        }
                    }
                })
    }
}
