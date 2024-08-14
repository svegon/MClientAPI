package io.github.svegon.mclientapi.client.event.render

import io.github.svegon.mclientapi.client.mixinterface.IRenderTickCounter
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

interface BlockEntityRenderCallback {
    fun onBlockEntityRender(
        blockEntity: BlockEntity, tickDelta: Float, matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider, callback: CallbackInfo
    )

    companion object {
        @JvmField
        val EVENT: Event<BlockEntityRenderCallback> = EventFactory.createArrayBacked(
            BlockEntityRenderCallback::class.java,
            object : BlockEntityRenderCallback {
                override fun onBlockEntityRender(
                    blockEntity: BlockEntity,
                    tickDelta: Float,
                    matrices: MatrixStack,
                    vertexConsumers: VertexConsumerProvider,
                    callback: CallbackInfo
                ) {}
            }
        ) { listeners: Array<BlockEntityRenderCallback> ->
            object : BlockEntityRenderCallback {
                override fun onBlockEntityRender(
                    blockEntity: BlockEntity,
                    tickDelta: Float,
                    matrices: MatrixStack,
                    vertexConsumers: VertexConsumerProvider,
                    callback: CallbackInfo
                ) {
                    for (listener in listeners) {
                        listener.onBlockEntityRender(blockEntity, tickDelta, matrices, vertexConsumers, callback)

                        if (callback.isCancelled) {
                            return
                        }
                    }
                }
            }
        }
    }
}
