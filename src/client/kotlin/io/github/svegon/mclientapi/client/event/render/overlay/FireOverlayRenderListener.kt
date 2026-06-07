package io.github.svegon.mclientapi.client.event.render.overlay

import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun interface FireOverlayRenderListener {
    fun onFireOverlayRender(
        poseStack: PoseStack, bufferSource: MultiBufferSource, sprite: TextureAtlasSprite, ci: CallbackInfo
    )

    companion object {
        val EVENT: Event<FireOverlayRenderListener> = EventFactory.createArrayBacked(
            FireOverlayRenderListener::class.java,
            FireOverlayRenderListener { poseStack: PoseStack, bufferSource: MultiBufferSource,
                                        sprite: TextureAtlasSprite, ci: CallbackInfo -> }
        ) { listeners: Array<FireOverlayRenderListener> ->
            FireOverlayRenderListener { poseStack: PoseStack, bufferSource: MultiBufferSource,
                                        sprite: TextureAtlasSprite, ci: CallbackInfo ->
                for (listener in listeners) {
                    listener.onFireOverlayRender(poseStack, bufferSource, sprite, ci)

                    if (ci.isCancelled) {
                        return@FireOverlayRenderListener
                    }
                }
            }
        }
    }
}
