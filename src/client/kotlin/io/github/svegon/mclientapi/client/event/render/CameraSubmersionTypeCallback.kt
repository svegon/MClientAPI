package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.enums.CameraSubmersionType
import net.minecraft.client.render.Camera
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function

fun interface CameraSubmersionTypeCallback {
    fun getCameraSubmersionType(camera: Camera, callback: CallbackInfoReturnable<CameraSubmersionType>)

    companion object {
        @JvmField
        val EVENT: Event<CameraSubmersionTypeCallback> = EventFactory.createArrayBacked(
            CameraSubmersionTypeCallback::class.java,
            CameraSubmersionTypeCallback { camera: Camera, callback: CallbackInfoReturnable<CameraSubmersionType> -> }
        ) { listeners: Array<CameraSubmersionTypeCallback> ->
            CameraSubmersionTypeCallback { camera: Camera, callback: CallbackInfoReturnable<CameraSubmersionType> ->
                for (listener in listeners) {
                    listener.getCameraSubmersionType(camera, callback)

                    if (callback.isCancelled()) {
                        return@CameraSubmersionTypeCallback
                    }
                }
            }
        }
    }
}
