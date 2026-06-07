package io.github.svegon.mclientapi.client.event.render.light

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.renderer.LightmapRenderStateExtractor
import net.minecraft.world.entity.LivingEntity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import kotlin.Array
import kotlin.Float

fun interface LightmapDarknessFactorListener {
    fun getDarknessFactor(lightingState: LightmapRenderStateExtractor, camera: LivingEntity, darknessGamma: Float,
                          partialTickTime: Float, cir: CallbackInfoReturnable<Float>)

    companion object {
        val EVENT: Event<LightmapDarknessFactorListener> = EventFactory.createArrayBacked(
            LightmapDarknessFactorListener::class.java, LightmapDarknessFactorListener {
                lightingState: LightmapRenderStateExtractor, camera: LivingEntity, darknessGamma: Float,
                                             partialTickTime: Float, cir: CallbackInfoReturnable<Float> -> }
        ) { listeners: Array<LightmapDarknessFactorListener> ->
            LightmapDarknessFactorListener { lightingState: LightmapRenderStateExtractor, camera: LivingEntity,
                                             darknessGamma: Float, partialTickTime: Float,
                                             cir: CallbackInfoReturnable<Float> ->
                for (listener in listeners) {
                    listener.getDarknessFactor(lightingState, camera, darknessGamma, partialTickTime, cir)

                    if (cir.isCancelled) {
                        return@LightmapDarknessFactorListener
                    }
                }
            }
        }
    }
}
