package io.github.svegon.mclientapi.client.mixin.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.svegon.mclientapi.client.event.render.entity.EntityLabelRenderListener;
import io.github.svegon.mclientapi.client.mixinterface.IEntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState>
        implements IEntityRenderer<T, S> {
    @Shadow protected abstract  <S extends EntityRenderState> void submitNameDisplay(
            final S state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector,
            final CameraRenderState camera, final int offset
    );

    @Override
    public void mClientAPI$renderLabel(@NotNull S state, @NotNull PoseStack poseStack,
                                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera,
                                       int offset) {
        submitNameDisplay(state, poseStack, submitNodeCollector, camera, offset);
    }

    @Redirect(method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;" +
            "Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;" +
            "Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            target = @Desc(owner = EntityRenderer.class, value = "submitNameDisplay",
            args = {EntityRenderState.class, PoseStack.class, SubmitNodeCollector.class, CameraRenderState.class,
                    int.class}), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submitNameDisplay(" +
                    "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;" +
                    "Lnet/minecraft/client/renderer/SubmitNodeCollector;" +
                    "Lnet/minecraft/client/renderer/state/level/CameraRenderState;I)V"))
    private static <T extends Entity, S extends EntityRenderState> void onSubmitNameDisplay(
            EntityRenderer<T, S> instance, S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera, int offset
    ) {
        CallbackInfoReturnable<S> stateCIR = new CallbackInfoReturnable<>("state", true, state);
        CallbackInfoReturnable<CameraRenderState> cameraCIR = new CallbackInfoReturnable<>("state",
                true, camera);
        CallbackInfoReturnable<Integer> offsetCIR = new CallbackInfoReturnable<>("state", true, offset);

        EntityLabelRenderListener.Companion.getEVENT().invoker().onEntityLabelRender(instance, stateCIR, poseStack,
                submitNodeCollector, cameraCIR, offsetCIR);

        ((IEntityRenderer<T, S>) instance).mClientAPI$renderLabel(stateCIR.getReturnValue(), poseStack,
                submitNodeCollector, cameraCIR.getReturnValue(), offsetCIR.getReturnValueI());
    }
}
