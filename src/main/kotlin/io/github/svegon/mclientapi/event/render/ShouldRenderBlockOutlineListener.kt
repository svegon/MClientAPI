package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.render.GameRenderer
import java.util.function.Function

interface ShouldRenderBlockOutlineListener {
    fun onShouldRenderBlockOutline(renderer: GameRenderer?, callback: CallbackInfoReturnable<Boolean?>?)

    companion object {
        val EVENT: Event<ShouldRenderBlockOutlineListener?> =
            EventFactory.createArrayBacked<ShouldRenderBlockOutlineListener?>(
                ShouldRenderBlockOutlineListener::class.java,
                ShouldRenderBlockOutlineListener { renderer: GameRenderer?, callback: CallbackInfoReturnable<Boolean?>? -> },
                Function<Array<ShouldRenderBlockOutlineListener?>, ShouldRenderBlockOutlineListener?> { listeners: Array<ShouldRenderBlockOutlineListener?>? ->
                    ShouldRenderBlockOutlineListener { renderer: GameRenderer?, callback: CallbackInfoReturnable<Boolean?>? ->
                        for (listener in listeners) {
                            listener.onShouldRenderBlockOutline(renderer, callback)

                            if (callback.isCancelled()) {
                                return@ShouldRenderBlockOutlineListener
                            }
                        }
                    }
                })
    }
}
