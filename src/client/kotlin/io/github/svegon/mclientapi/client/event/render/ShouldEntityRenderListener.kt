package io.github.svegon.mclientapi.client.event.render

import io.github.svegon.utils.math.geometry.vector.Vec3d
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.render.Camera
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.function.Function

fun interface ShouldEntityRenderListener {
    fun shouldRenderEntity(
        dispatcher: EntityRenderDispatcher,
        entity: Entity,
        frustum: Frustum,
        cameraPos: Vec3d,
        callback: CallbackInfoReturnable<Boolean>
    )

    companion object {
        @JvmField
        val EVENT: Event<ShouldEntityRenderListener> = EventFactory.createArrayBacked(
            ShouldEntityRenderListener::class.java,
            ShouldEntityRenderListener { dispatcher: EntityRenderDispatcher,
                                         entity: Entity,
                                         frustum: Frustum,
                                         cameraPos: Vec3d,
                                         callback: CallbackInfoReturnable<Boolean> -> }
        ) { listeners: Array<ShouldEntityRenderListener> ->
            ShouldEntityRenderListener { dispatcher: EntityRenderDispatcher,
                                         entity: Entity,
                                         frustum: Frustum,
                                         cameraPos: Vec3d,
                                         callback: CallbackInfoReturnable<Boolean> ->
                for (listener in listeners) {
                    listener.shouldRenderEntity(dispatcher, entity, frustum, cameraPos, callback)

                    if (callback.isCancelled) {
                        return@ShouldEntityRenderListener
                    }
                }
            }
        }
    }
}
