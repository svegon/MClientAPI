package io.github.svegon.mclientapi.client.event.render.overlay

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.Camera
import net.minecraft.world.level.material.FogType
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface CameraSubmersionTypeCallback {
    fun getCameraSubmersionType(camera: Camera, callback: CallbackInfoReturnable<FogType>)

    companion object {
        val EVENT: Event<CameraSubmersionTypeCallback> = EventFactory.createArrayBacked(
            CameraSubmersionTypeCallback::class.java,
            CameraSubmersionTypeCallback { camera: Camera, callback: CallbackInfoReturnable<FogType> -> }
        ) { listeners: Array<CameraSubmersionTypeCallback> ->
            CameraSubmersionTypeCallback { camera: Camera, callback: CallbackInfoReturnable<FogType> ->
                for (listener in listeners) {
                    listener.getCameraSubmersionType(camera, callback)

                    if (callback.isCancelled) {
                        return@CameraSubmersionTypeCallback
                    }
                }
            }
        }
    }
}
