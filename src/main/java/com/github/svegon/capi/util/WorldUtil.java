package com.github.svegon.capi.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class WorldUtil {
    private WorldUtil() {
        throw new UnsupportedOperationException();
    }

    public static HitResult raycast(final @NotNull Entity camera, double reachDistance, float tickDelta,
                                    Predicate<Entity> hittableEntities, boolean fluids) {
        BlockHitResult blockHitResult = (BlockHitResult) camera.raycast(reachDistance, tickDelta, fluids);
        Vec3d cameraPos = camera.getCameraPosVec(tickDelta);
        boolean bl = false;
        double squaredReach = reachDistance;

        if (reachDistance >= 5) {
            reachDistance = squaredReach = 6.0D;
        } else if (reachDistance > 3.0D) {
            bl = true;
        }

        squaredReach *= squaredReach;
        Vec3d rotVec = camera.getRotationVec(1.0F);
        Vec3d max = cameraPos.add(rotVec.x * reachDistance, rotVec.y * reachDistance,
                rotVec.z * reachDistance);
        Box box = camera.getBoundingBox().stretch(rotVec.multiply(reachDistance)).expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(camera, cameraPos, max, box, hittableEntities,
                squaredReach);

        if (entityHitResult != null) {
            Vec3d targetEntityPos = entityHitResult.getPos();
            double d = cameraPos.squaredDistanceTo(targetEntityPos);

            if (bl && d > 9.0D) {
                return BlockHitResult.createMissed(targetEntityPos,
                        Direction.getFacing(rotVec.x, rotVec.y, rotVec.z), GeometryUtil.toBlockPos(targetEntityPos));
            } else if (d < squaredReach) {
                return entityHitResult;
            }
        }

        return blockHitResult;
    }

    public static HitResult raycast(final @NotNull Entity camera, double reachDistance, float tickDelta,
                                    Predicate<Entity> hittableEntities) {
        return raycast(camera, reachDistance, tickDelta, hittableEntities, false);
    }

    public static HitResult raycast(final @NotNull Entity camera, double reachDistance, float tickDelta) {
        return raycast(camera, reachDistance, tickDelta, e -> e.canHit() && !e.isSpectator());
    }

    public static HitResult raycast(final @NotNull Entity camera, double reachDistance) {
        return raycast(camera, reachDistance, MinecraftClient.getInstance().getTickDelta());
    }

    public static BlockHitResult raycast(World world, Entity player, double reach,
                                         RaycastContext.FluidHandling fluidHandling) {
        float yaw = player.getPitch();
        float pitch = player.getYaw();
        Vec3d eyePos = player.getEyePos();
        float h = MathHelper.cos(-pitch * ((float)Math.PI / 180) - (float)Math.PI);
        float i = MathHelper.sin(-pitch * ((float)Math.PI / 180) - (float)Math.PI);
        float j = -MathHelper.cos(-yaw * ((float)Math.PI / 180));
        double normalY = MathHelper.sin(-yaw * ((float)Math.PI / 180));
        double normalX = i * j;
        double normalZ = h * j;
        Vec3d endPos = eyePos.add(normalX * reach, normalY * reach, normalZ * reach);
        return world.raycast(new RaycastContext(eyePos, endPos, RaycastContext.ShapeType.OUTLINE, fluidHandling,
                player));
    }

    public static BlockHitResult raycast(World world, Entity player, RaycastContext.FluidHandling fluidHandling) {
        return raycast(world, player, 5, fluidHandling);
    }
}
