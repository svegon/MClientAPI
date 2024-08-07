package io.github.svegon.capi.event.render

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.render.VertexConsumerProvider
import java.util.function.Function

interface BlockEntityRenderCallback {
    fun onBlockEntityRender(
        blockEntity: BlockEntity?, tickDelta: Float, matrices: MatrixStack?,
        vertexConsumers: VertexConsumerProvider?, callback: CallbackInfo?
    )

    companion object {
        @JvmField
        val EVENT: Event<BlockEntityRenderCallback?> = EventFactory.createArrayBacked<BlockEntityRenderCallback?>(
            BlockEntityRenderCallback::class.java,
            BlockEntityRenderCallback { blockEntity: BlockEntity?, tickDelta: Float, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, callback: CallbackInfo? -> },
            Function<Array<BlockEntityRenderCallback?>, BlockEntityRenderCallback?> { listeners: Array<BlockEntityRenderCallback?>? ->
                BlockEntityRenderCallback { blockEntity: BlockEntity?, tickDelta: Float, matrices: MatrixStack?, vertexConsumers: VertexConsumerProvider?, callback: CallbackInfo? ->
                    for (listener in listeners) {
                        listener.onBlockEntityRender(blockEntity, tickDelta, matrices, vertexConsumers, callback)

                        if (callback.isCancelled()) {
                            return@BlockEntityRenderCallback
                        }
                    }
                }
            })
    }
}
