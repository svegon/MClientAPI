package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.EntityHasOutlineCallback;
import io.github.svegon.mclientapi.client.mixinterface.IWorldRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements SynchronousResourceReloader, AutoCloseable, IWorldRenderer {
    @Shadow
    @Nullable
    private ClientWorld world;

    @Shadow
    private void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity,
                                  double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state) {
        throw new AssertionError();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;hasOutline(Lnet/minecraft/entity/Entity;)Z"))
    public boolean shouldDrawOutline(MinecraftClient minecraftClient, Entity entity) {
        CallbackInfoReturnable<Boolean> callback = new CallbackInfoReturnable<>("hasOutline callback",
                true, minecraftClient.hasOutline(entity));

        EntityHasOutlineCallback.EVENT.invoker().hasEntityOutline(entity, callback);

        return callback.getReturnValueZ();
    }

    @Override
    public void mClientAPI$drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Camera camera,
                                            BlockPos pos) {
        Vec3d cameraPos = camera.getPos();

        drawBlockOutline(matrices, vertexConsumer, camera.getFocusedEntity(), cameraPos.getX(), cameraPos.getY(),
                cameraPos.getZ(), pos, world.getBlockState(pos));
    }
}
