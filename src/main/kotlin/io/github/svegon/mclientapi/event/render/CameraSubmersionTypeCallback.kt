package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.render.Camera
import java.util.function.Function

interface CameraSubmersionTypeCallback {
    fun getCameraSubmersionType(camera: Camera?, callback: CallbackInfoReturnable<CameraSubmersionType?>?)

    companion object {
        @JvmField
        val EVENT: Event<CameraSubmersionTypeCallback?> = EventFactory.createArrayBacked<CameraSubmersionTypeCallback?>(
            CameraSubmersionTypeCallback::class.java,
            CameraSubmersionTypeCallback { camera: Camera?, callback: CallbackInfoReturnable<CameraSubmersionType?>? -> },
            Function<Array<CameraSubmersionTypeCallback?>, CameraSubmersionTypeCallback?> { listeners: Array<CameraSubmersionTypeCallback?>? ->
                CameraSubmersionTypeCallback { camera: Camera?, callback: CallbackInfoReturnable<CameraSubmersionType?>? ->
                    for (listener in listeners) {
                        listener.getCameraSubmersionType(camera, callback)

                        if (callback.isCancelled()) {
                            return@CameraSubmersionTypeCallback
                        }
                    }
                }
            })
    }
}
