package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.entity.Entity
import net.minecraft.text.Text

fun interface EntityLabelRenderListener {
    fun onEntityLabelRender(
        entity: Entity?, text: CallbackInfoReturnable<Text?>?, matrices: MatrixStack?,
        vertexConsumers: VertexConsumerProvider?, light: Int, ci: CallbackInfoReturnable<Boolean?>?
    )

    companion object {
        @JvmField
        val EVENT: Event<EntityLabelRenderListener> = EventFactory.createArrayBacked<EntityLabelRenderListener>(
            EntityLabelRenderListener::class.java,
            EntityLabelRenderListener { entity: Entity?, text: CallbackInfoReturnable<Text?>?, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, light: Int, ci: CallbackInfoReturnable<Boolean?>? -> }
        ) { listeners: Array<EntityLabelRenderListener> ->
            EntityLabelRenderListener { entity: Entity?, text: CallbackInfoReturnable<Text?>?, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, light: Int, ci: CallbackInfoReturnable<Boolean?> ->
                for (listener in listeners) {
                    listener.onEntityLabelRender(entity, text, matrices, vertexConsumers, light, ci)

                    if (ci.isCancelled()) {
                        return@EntityLabelRenderListener
                    }
                }
            }
        }
    }
}
