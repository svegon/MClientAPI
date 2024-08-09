package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.EntityLabelRenderListener;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Shadow
    @Final
    protected EntityRenderDispatcher dispatcher;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow protected abstract boolean hasLabel(T entity);

    @Shadow protected abstract void renderLabelIfPresent(T entity, Text text, MatrixStack matrices,
                                                         VertexConsumerProvider vertexConsumers, int light,
                                                         float tickDelta);

    @Inject(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/EntityRenderer;hasLabel(Lnet/minecraft/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void onRenderLabel(T entity, float yaw, float tickDelta, MatrixStack matrices,
                                        VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        CallbackInfoReturnable<Text> textOverride = new CallbackInfoReturnable<>(
                "Lnet/minecraft/client/render/entity/EntityRenderer;onRenderLabelIfPresent text override",
                true, entity.getDisplayName());
        CallbackInfoReturnable<Boolean> hasLabel = new CallbackInfoReturnable<>(ci.getId(), true,
                hasLabel(entity));

        EntityLabelRenderListener.EVENT.invoker().onEntityLabelRender(entity, tickDelta, matrices, vertexConsumers,
                light, textOverride, hasLabel);

        if (!textOverride.isCancelled() && !hasLabel.isCancelled()) {
            return;
        }

        ci.cancel();

        if (!hasLabel.getReturnValueZ()) {
            return;
        }

        renderLabelIfPresent(entity, textOverride.getReturnValue(), matrices, vertexConsumers, light, tickDelta);
    }
}
