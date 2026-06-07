package io.github.svegon.mclientapi.client.event.render.overlay

import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface UnderwaterOverlayRenderListener {
    fun onUnderwaterOverlayRender(
        minecraft: Minecraft, poseStack: PoseStack, bufferSource: MultiBufferSource, ci: CallbackInfo
    )

    companion object {
        val EVENT: Event<UnderwaterOverlayRenderListener> = EventFactory.createArrayBacked(
            UnderwaterOverlayRenderListener::class.java,
                UnderwaterOverlayRenderListener { minecraft: Minecraft, poseStack: PoseStack,
                                                  bufferSource: MultiBufferSource, ci: CallbackInfo -> }
        ) { listeners: Array<UnderwaterOverlayRenderListener> ->
            UnderwaterOverlayRenderListener { minecraft: Minecraft, poseStack: PoseStack,
                                              bufferSource: MultiBufferSource, ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.onUnderwaterOverlayRender(minecraft, poseStack, bufferSource, ci)

                    if (ci.isCancelled) {
                        return@UnderwaterOverlayRenderListener
                    }
                }
            }
        }
    }
}