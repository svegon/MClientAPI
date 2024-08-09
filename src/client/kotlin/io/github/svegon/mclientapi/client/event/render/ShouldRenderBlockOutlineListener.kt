package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.render.GameRenderer
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface ShouldRenderBlockOutlineListener {
    fun onShouldRenderBlockOutline(renderer: GameRenderer, callback: CallbackInfoReturnable<Boolean>)

    companion object {
        @JvmField
        val EVENT: Event<ShouldRenderBlockOutlineListener> =
            EventFactory.createArrayBacked(
                ShouldRenderBlockOutlineListener::class.java,
                ShouldRenderBlockOutlineListener { renderer: GameRenderer,
                                                   callback: CallbackInfoReturnable<Boolean> -> }
            ) { listeners: Array<ShouldRenderBlockOutlineListener> ->
                ShouldRenderBlockOutlineListener { renderer: GameRenderer, callback: CallbackInfoReturnable<Boolean> ->
                    for (listener in listeners) {
                        listener.onShouldRenderBlockOutline(renderer, callback)

                        if (callback.isCancelled) {
                            return@ShouldRenderBlockOutlineListener
                        }
                    }
                }
            }
    }
}
