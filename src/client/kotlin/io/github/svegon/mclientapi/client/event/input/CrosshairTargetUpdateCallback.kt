package io.github.svegon.mclientapi.client.event.input

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.Minecraft
import net.minecraft.world.phys.HitResult
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface CrosshairTargetUpdateCallback {
    fun onCrosshairTargetUpdate(client: Minecraft, tickDelta: Float, cir: CallbackInfoReturnable<HitResult>)

    companion object {
        val EVENT: Event<CrosshairTargetUpdateCallback> = EventFactory.createArrayBacked(
                CrosshairTargetUpdateCallback::class.java,
                CrosshairTargetUpdateCallback { client: Minecraft, tickDelta: Float,
                                                cir: CallbackInfoReturnable<HitResult> -> }
        ) { listeners: Array<CrosshairTargetUpdateCallback> ->
            CrosshairTargetUpdateCallback { client: Minecraft, tickDelta: Float,
                                            cir: CallbackInfoReturnable<HitResult> ->
                for (listener in listeners) {
                    listener.onCrosshairTargetUpdate(client, tickDelta, cir)

                    if (cir.isCancelled) {
                        return@CrosshairTargetUpdateCallback
                    }
                }
            }
        }
    }
}
