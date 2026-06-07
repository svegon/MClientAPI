package io.github.svegon.mclientapi.client.event.render.entity

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRenderDispatcher
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface ShouldEntityRenderListener {
    fun shouldRenderEntity(
        dispatcher: EntityRenderDispatcher,
        entity: Entity,
        culler: Frustum,
        cameraPos: Vec3,
        cir: CallbackInfoReturnable<Boolean>
    )

    companion object {
        val EVENT: Event<ShouldEntityRenderListener> = EventFactory.createArrayBacked(
            ShouldEntityRenderListener::class.java,
            ShouldEntityRenderListener { dispatcher: EntityRenderDispatcher,
                                         entity: Entity,
                                         frustum: Frustum,
                                         cameraPos: Vec3,
                                         callback: CallbackInfoReturnable<Boolean> -> }
        ) { listeners: Array<ShouldEntityRenderListener> ->
            ShouldEntityRenderListener { dispatcher: EntityRenderDispatcher,
                                         entity: Entity,
                                         frustum: Frustum,
                                         cameraPos: Vec3,
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