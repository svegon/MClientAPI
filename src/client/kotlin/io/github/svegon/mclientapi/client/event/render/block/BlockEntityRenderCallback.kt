package io.github.svegon.mclientapi.client.event.render.block

import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.state.level.CameraRenderState
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface BlockEntityRenderCallback {
    fun onBlockEntityRender(
        dispatcher: BlockEntityRenderDispatcher, state: BlockEntityRenderState, poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector, cameraState: CameraRenderState, ci: CallbackInfo
    )

    companion object {
        val EVENT: Event<BlockEntityRenderCallback> = EventFactory.createArrayBacked(
            BlockEntityRenderCallback::class.java, BlockEntityRenderCallback {
                    dispatcher: BlockEntityRenderDispatcher, state: BlockEntityRenderState, poseStack: PoseStack,
                    submitNodeCollector: SubmitNodeCollector, cameraState: CameraRenderState, ci: CallbackInfo -> }
        ) { callbacks: Array<BlockEntityRenderCallback> -> BlockEntityRenderCallback {
                dispatcher: BlockEntityRenderDispatcher, state: BlockEntityRenderState, poseStack: PoseStack,
                submitNodeCollector: SubmitNodeCollector, cameraState: CameraRenderState, ci: CallbackInfo ->
                for (callback in callbacks) {
                    callback.onBlockEntityRender(dispatcher, state, poseStack, submitNodeCollector, cameraState, ci)

                    if (ci.isCancelled) {
                        return@BlockEntityRenderCallback
                    }
                }
            }
        }
    }
}