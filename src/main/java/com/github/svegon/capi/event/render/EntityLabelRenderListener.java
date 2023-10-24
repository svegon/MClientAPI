package com.github.svegon.capi.event.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@FunctionalInterface
public interface EntityLabelRenderListener {
    Event<EntityLabelRenderListener> EVENT = EventFactory.createArrayBacked(EntityLabelRenderListener.class,
            (entity, text, matrices, vertexConsumers, light, ci) -> {},
            listeners -> (entity, text, matrices, vertexConsumers, light, ci) -> {
        for (EntityLabelRenderListener listener : listeners) {
            listener.onEntityLabelRender(entity, text, matrices, vertexConsumers, light, ci);

            if (ci.isCancelled()) {
                return;
            }
        }
    });

    void onEntityLabelRender(Entity entity, CallbackInfoReturnable<Text> text, MatrixStack matrices,
                             VertexConsumerProvider vertexConsumers, int light, CallbackInfoReturnable<Boolean> ci);
}
