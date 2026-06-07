package io.github.svegon.mclientapi.client.event.render.entity

import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.world.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

interface EntityLabelRenderListener {
    fun <T: Entity, S: EntityRenderState> onEntityLabelRender(
        renderer: EntityRenderer<T, S>, state: CallbackInfoReturnable<S>, poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector, camera: CallbackInfoReturnable<CameraRenderState>,
        offset: CallbackInfoReturnable<Int>
    )

    companion object {
        val EVENT: Event<EntityLabelRenderListener> = EventFactory.createArrayBacked(
            EntityLabelRenderListener::class.java,
            object : EntityLabelRenderListener {
                override fun <T : Entity, S : EntityRenderState> onEntityLabelRender(
                    renderer: EntityRenderer<T, S>,
                    state: CallbackInfoReturnable<S>,
                    poseStack: PoseStack,
                    submitNodeCollector: SubmitNodeCollector,
                    camera: CallbackInfoReturnable<CameraRenderState>,
                    offset: CallbackInfoReturnable<Int>
                ) {}
            }
        ) { listeners: Array<EntityLabelRenderListener> ->
            object : EntityLabelRenderListener {
                override fun <T : Entity, S : EntityRenderState> onEntityLabelRender(
                    renderer: EntityRenderer<T, S>,
                    state: CallbackInfoReturnable<S>,
                    poseStack: PoseStack,
                    submitNodeCollector: SubmitNodeCollector,
                    camera: CallbackInfoReturnable<CameraRenderState>,
                    offset: CallbackInfoReturnable<Int>
                ) {
                    for (listener in listeners) {
                        listener.onEntityLabelRender(renderer, state, poseStack, submitNodeCollector, camera, offset)

                        if (state.isCancelled || camera.isCancelled || offset.isCancelled) {
                            return
                        }
                    }
                }
            }
        }
    }
}