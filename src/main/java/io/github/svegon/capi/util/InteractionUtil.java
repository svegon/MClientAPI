package io.github.svegon.capi.util;

import com.google.common.util.concurrent.AtomicDouble;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public final class InteractionUtil {
    private InteractionUtil() {
        throw new UnsupportedOperationException();
    }

    private static final Direction[] DIRECTIONS = Direction.values();

    public static BlockHitResult targetBlock(@NotNull final Entity camera, @NotNull final BlockPos blockPos,
                                             final double reachDistance, final boolean fluids) {
        final float yaw = camera.getYaw();
        final float pitch = camera.getPitch();
        final World world = camera.getWorld();
        final Vec3d eyePos = camera.getEyePos();
        final VoxelShape shape = world.getBlockState(blockPos).getOutlineShape(world, blockPos);
        final AtomicReference<BlockHitResult> collision = new AtomicReference<>(shape.raycast(eyePos,
                camera.getRotationVec(1), blockPos));
        final AtomicDouble sqDistanceToCollision = new AtomicDouble(Double.MAX_VALUE);

        if (collision.get() != null) {
            return collision.get();
        }

        shape.forEachEdge(((minX, minY, minZ, maxX, maxY, maxZ) -> {
            Vec3d[] corners = {new Vec3d(minX, minY, minZ), new Vec3d(minX, minY, maxZ), new Vec3d(minX, maxY, minZ),
                    new Vec3d(minX, maxY, maxZ), new Vec3d(maxX, minY, minZ), new Vec3d(maxX, minY, maxZ),
                    new Vec3d(maxX, maxY, minZ), new Vec3d(maxX, maxY, maxZ)};

            for (Vec3d corner : corners) {
                Vec2f rot = RotationUtil.lookVector(eyePos, corner);

                camera.setYaw(rot.y);
                camera.setPitch(rot.x);

                HitResult hitResult = WorldUtil.raycast(camera, reachDistance, 1,
                        e -> e.canHit() && !e.isSpectator(), fluids);

                if (hitResult.getType() == HitResult.Type.BLOCK
                        && ((BlockHitResult) hitResult).getBlockPos().equals(blockPos)) {
                    double distanceSq = eyePos.squaredDistanceTo(hitResult.getPos());

                    if (distanceSq < sqDistanceToCollision.get()) {
                        collision.set((BlockHitResult) hitResult);
                        sqDistanceToCollision.set(distanceSq);
                    }
                }
            }
        }));
        camera.setYaw(yaw);
        camera.setPitch(pitch);

        Vec3d center = Vec3d.ofCenter(blockPos);

        return collision.get() != null ? collision.get() : BlockHitResult.createMissed(center,
                RotationUtil.getFacing(eyePos.subtract(center)), blockPos);
    }

    public static BlockHitResult targetBlock(@NotNull final Entity entity, @NotNull final BlockPos blockPos,
                                             final double reachDistance) {
        return targetBlock(entity, blockPos, reachDistance, false);
    }

    public static ObjectObjectImmutablePair<BlockHitResult, Vec2f> targetBlockNoClip(@NotNull final WorldView world,
                                                                                     @NotNull final Vec3d eyePos,
                                                                                     @NotNull final BlockPos blockPos) {
        final BlockState state = world.getBlockState(blockPos);
        final VoxelShape shape = state.getOutlineShape(world, blockPos);
        final Vec3d relativeEyePos = eyePos.subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        if (shape.getBoundingBoxes().parallelStream().anyMatch(box -> box.contains(relativeEyePos))) {
            return new ObjectObjectImmutablePair<>(new BlockHitResult(eyePos, RotationUtil.getFacing(eyePos),
                    blockPos, true), Vec2f.ZERO);
        }

        Optional<Vec3d> optional = shape.getClosestPointTo(relativeEyePos);
        Vec3d collisionPoint = optional.orElseGet(() -> new Vec3d(0.5, 0.5, 0.5))
                 .add(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        Vec2f rotation = RotationUtil.lookVector(eyePos, collisionPoint);
        Direction side = Direction.fromRotation(rotation.y).getOpposite();

        return new ObjectObjectImmutablePair<>(optional.isEmpty() ? BlockHitResult.createMissed(collisionPoint,
                side, blockPos) : new BlockHitResult(collisionPoint, side, blockPos, false), rotation);
    }

    public static ObjectObjectImmutablePair<BlockHitResult, Vec2f> targetBlockNoClip(@NotNull final WorldView world,
                                                                                     @NotNull final Entity entity,
                                                                                     @NotNull final BlockPos blockPos) {
        return targetBlockNoClip(world, entity.getEyePos(), blockPos);
    }

    @Nullable
    public static EntityHitResult targetEntity(@NotNull final Entity camera, @NotNull final Entity entity,
                                             final double reachDistance, final boolean fluids) {
        HitResult currentTarget = WorldUtil.raycast(camera, reachDistance, 1,
                e -> e == entity || (e.canHit() && !e.isSpectator()), fluids);

        if (currentTarget instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() == entity) {
            return entityHitResult;
        }

        final float yaw = camera.getYaw();
        final float pitch = camera.getPitch();
        final Vec3d eyePos = entity.getEyePos();
        final Box bb = entity.getBoundingBox();
        final Vec3d[] corners = {new Vec3d(bb.minX, bb.minY, bb.minZ), new Vec3d(bb.minX, bb.minY, bb.maxZ),
                new Vec3d(bb.minX, bb.maxY, bb.minZ), new Vec3d(bb.minX, bb.maxY, bb.maxZ),
        new Vec3d(bb.maxX, bb.minY, bb.minZ), new Vec3d(bb.maxX, bb.minY, bb.maxZ),
                new Vec3d(bb.maxX, bb.maxY, bb.minZ), new Vec3d(bb.maxX, bb.maxY, bb.maxZ)};
        EntityHitResult collision = null;
        double sqDistanceToCollision = Double.MAX_VALUE;

        for (Vec3d corner : corners) {
            Vec2f rot = RotationUtil.lookVector(eyePos, corner);

            camera.setYaw(rot.y);
            camera.setPitch(rot.x);

            HitResult hitResult = WorldUtil.raycast(camera, reachDistance, 1,
                    e -> e.canHit() && !e.isSpectator(), fluids);

            if (hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() == entity) {
                double distanceSq = eyePos.squaredDistanceTo(entityHitResult.getPos());

                if (distanceSq < sqDistanceToCollision) {
                    collision = entityHitResult;
                    sqDistanceToCollision = distanceSq;
                }
            }
        }

        camera.setYaw(yaw);
        camera.setPitch(pitch);

        return collision;
    }

    @Nullable
    public static EntityHitResult targetEntity(@NotNull final Entity camera, @NotNull final Entity entity,
                                              final double reachDistance) {
        return targetEntity(camera, entity, reachDistance, false);
    }

    @Environment(EnvType.CLIENT)
    public static ActionResult useItem(@Nullable HitResult crosshairTarget, ClientPlayerEntity player,
                                       ClientPlayerInteractionManager interactionManager, GameRenderer gameRenderer) {
        ActionResult result;

        for (Hand hand : Hand.values()) {
            ItemStack itemStack = player.getStackInHand(hand);

            if (!itemStack.isItemEnabled(player.clientWorld.getEnabledFeatures())) {
                return ActionResult.FAIL;
            }

            if (crosshairTarget != null) {
                switch (crosshairTarget.getType()) {
                    case ENTITY -> {
                        EntityHitResult entityHitResult = (EntityHitResult) crosshairTarget;
                        Entity entity = entityHitResult.getEntity();

                        if (!player.clientWorld.getWorldBorder().contains(entity.getBlockPos())) {
                            return ActionResult.FAIL;
                        }

                        ActionResult actionResult = interactionManager.interactEntityAtLocation(player, entity,
                                entityHitResult, hand);

                        if (!actionResult.isAccepted()) {
                            actionResult = interactionManager.interactEntity(player, entity, hand);
                        }

                        if (!actionResult.isAccepted()) {
                            break;
                        }

                        if (actionResult.shouldSwingHand()) {
                            player.swingHand(hand);
                        }

                        return actionResult;
                    }
                    case BLOCK -> {
                        BlockHitResult blockHitResult = (BlockHitResult) crosshairTarget;
                        int i = itemStack.getCount();
                        ActionResult actionResult2 = interactionManager.interactBlock(player, hand, blockHitResult);

                        if (actionResult2.isAccepted()) {
                            if (actionResult2.shouldSwingHand()) {
                                player.swingHand(hand);

                                if (!itemStack.isEmpty() && (itemStack.getCount() != i
                                        || interactionManager.hasCreativeInventory())) {
                                    gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                                }
                            }

                            return actionResult2;
                        }

                        if (actionResult2 != ActionResult.FAIL) {
                            break;
                        }

                        return actionResult2;
                    }
                }
            }

            if (itemStack.isEmpty() || !(result = interactionManager.interactItem(player, hand)).isAccepted()) {
                continue;
            }

            if (result.shouldSwingHand()) {
                player.swingHand(hand);
            }

            gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
            return result;
        }

        return ActionResult.PASS;
    }

    @Environment(EnvType.CLIENT)
    public static ActionResult useItem(MinecraftClient client) {
        return useItem(client.crosshairTarget, client.player, client.interactionManager, client.gameRenderer);
    }

    @Environment(EnvType.CLIENT)
    public static ActionResult useItem() {
        return useItem(MinecraftClient.getInstance());
    }
}
