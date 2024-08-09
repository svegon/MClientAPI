package io.github.svegon.mclientapi.client.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun interface EntityLabelRenderListener {
    fun onEntityLabelRender(
        entity: Entity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int,
        text: CallbackInfoReturnable<Text>, callback: CallbackInfoReturnable<Boolean>
    )

    companion object {
        @JvmField
        val EVENT: Event<EntityLabelRenderListener> = EventFactory.createArrayBacked(
            EntityLabelRenderListener::class.java,
            EntityLabelRenderListener { entity, tickDelta, matrices, vertexConsumers, light, text, callback -> }
        ) { listeners: Array<EntityLabelRenderListener> ->
            object : EntityLabelRenderListener {
                override fun onEntityLabelRender(
                    entity: Entity,
                    tickDelta: Float,
                    matrices: MatrixStack,
                    vertexConsumers: VertexConsumerProvider,
                    light: Int,
                    text: CallbackInfoReturnable<Text>,
                    callback: CallbackInfoReturnable<Boolean>
                ) {
                    for (listener in listeners) {
                        listener.onEntityLabelRender(entity, tickDelta, matrices, vertexConsumers, light, text,
                            callback)

                        if (callback.isCancelled) {
                            return
                        }
                    }
                }
            }
        }
    }
}
