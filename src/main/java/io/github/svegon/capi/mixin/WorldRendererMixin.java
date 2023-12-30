package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.render.EntityHasOutlineCallback;
import io.github.svegon.capi.event.render.ShouldEntityRenderCallback;
import io.github.svegon.capi.mixininterface.IWorldRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements SynchronousResourceReloader, AutoCloseable, IWorldRenderer {
    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;
    @Shadow
    @Nullable
    private ClientWorld world;
    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;
    @Shadow
    private int regularEntityCount;
    @Shadow
    private int blockEntityCount;
    @Shadow
    private Frustum frustum;
    @Shadow
    private boolean shouldCaptureFrustum;
    @Shadow
    @Nullable
    private Frustum capturedFrustum;
    @Shadow
    @Final
    private Vector4f[] capturedFrustumOrientation;
    @Shadow
    @Final
    private Vector3d capturedFrustumPosition;

    @Override
    public void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Camera camera, BlockPos pos) {
        Vec3d cameraPos = camera.getPos();

        drawBlockOutline(matrices, vertexConsumer, camera.getFocusedEntity(), cameraPos.getX(), cameraPos.getY(),
                cameraPos.getZ(), pos, world.getBlockState(pos));
    }

    @Override
    public void drawBlockOutline(MatrixStack matrices, Camera camera, BlockPos pos) {
        Vec3d cameraPos = camera.getPos();

        drawBlockOutline(matrices, bufferBuilders.getEntityVertexConsumers().getBuffer(RenderLayer.getLines()),
                camera, pos);
        renderLayer(RenderLayer.getLines(), matrices, cameraPos.getX(), cameraPos.getY(), cameraPos.getZ(),
                RenderSystem.getProjectionMatrix());
    }

    @Shadow
    private void renderLayer(RenderLayer renderLayer, MatrixStack matrices, double cameraX, double cameraY,
                             double cameraZ, Matrix4f positionMatrix) {

    }

    @Shadow
    private void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity,
                                  double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state) {

    }

    @Shadow public abstract boolean isRenderingReady(BlockPos pos);

    @Shadow protected abstract boolean canDrawEntityOutlines();

    @Shadow protected abstract void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ,
                                                 float tickDelta, MatrixStack matrices,
                                                 VertexConsumerProvider vertexConsumers);

    @Inject(method = "render", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/render/WorldRenderer;entityRenderDispatcher:" +
                    "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;",
            opcode = Opcodes.GETFIELD, ordinal = 1), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void shouldSkipEntityRender(MatrixStack matrices, float tickDelta, long limitTime,
                                        boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                                        LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix,
                                        CallbackInfo ci, Profiler profiler, Vec3d vec3d, double d, double e, double f,
                                        Matrix4f matrix4f, boolean bl2, Frustum frustum, float g, boolean bl22,
                                        boolean bl3, VertexConsumerProvider.Immediate immediate, Iterator<Entity> itr,
                                        Entity entity) {
        CallbackInfoReturnable<Boolean> callback = new CallbackInfoReturnable<>("should render entity callback",
                true);

        ShouldEntityRenderCallback.EVENT.invoker().shouldRenderEntity(entity, frustum, camera, callback);

        if (callback.isCancelled()) {
            if (callback.getReturnValueZ()) {
                VertexConsumerProvider vertexConsumerProvider;
                ++this.regularEntityCount;

                if (entity.age == 0) {
                    entity.lastRenderX = entity.getX();
                    entity.lastRenderY = entity.getY();
                    entity.lastRenderZ = entity.getZ();
                }

                if (this.canDrawEntityOutlines() && shouldDrawOutline(client, entity)) {
                    bl3 = true;
                    OutlineVertexConsumerProvider outlineVertexConsumerProvider =
                            this.bufferBuilders.getOutlineVertexConsumers();
                    vertexConsumerProvider = outlineVertexConsumerProvider;
                    int i = entity.getTeamColorValue();
                    outlineVertexConsumerProvider.setColor(ColorHelper.Argb.getRed(i), ColorHelper.Argb.getGreen(i), ColorHelper.Argb.getBlue(i), 255);
                } else {
                    vertexConsumerProvider = immediate;
                }
                this.renderEntity(entity, d, e, f, tickDelta, matrices, vertexConsumerProvider);
            }

            entity = client.player;
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;hasOutline(Lnet/minecraft/entity/Entity;)Z"))
    public boolean shouldDrawOutline(MinecraftClient minecraftClient, Entity entity) {
        CallbackInfoReturnable<Boolean> callback = new CallbackInfoReturnable<>("hasOutline callback",
                true, minecraftClient.hasOutline(entity));

        EntityHasOutlineCallback.EVENT.invoker().hasEntityOutline(entity, callback);

        return callback.getReturnValueZ();
    }
}
