package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.render.Camera
import net.minecraft.entity.Entity
import java.util.function.Function

interface ShouldEntityRenderCallback {
    fun shouldRenderEntity(
        entity: Entity?,
        frustum: Frustum?,
        camera: Camera?,
        callback: CallbackInfoReturnable<Boolean?>?
    )

    companion object {
        @JvmField
        val EVENT: Event<ShouldEntityRenderCallback?> = EventFactory.createArrayBacked<ShouldEntityRenderCallback?>(
            ShouldEntityRenderCallback::class.java,
            ShouldEntityRenderCallback { entity: Entity?, frustum: Frustum?, camera: Camera?, callback: CallbackInfoReturnable<Boolean?>? -> },
            Function<Array<ShouldEntityRenderCallback?>, ShouldEntityRenderCallback?> { listeners: Array<ShouldEntityRenderCallback?>? ->
                ShouldEntityRenderCallback { entity: Entity?, frustum: Frustum?, camera: Camera?, callback: CallbackInfoReturnable<Boolean?>? ->
                    for (listener in listeners) {
                        listener.shouldRenderEntity(entity, frustum, camera, callback)

                        if (callback.isCancelled()) {
                            return@ShouldEntityRenderCallback
                        }
                    }
                }
            })
    }
}
