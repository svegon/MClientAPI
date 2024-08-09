package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

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
