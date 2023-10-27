package io.github.svegon.capi.util;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public final class RotationUtil {
    private RotationUtil() {
        throw new UnsupportedOperationException();
    }

    public static Direction getFacing(@NotNull final Vec3d pos) {
        return Direction.getFacing(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * @see net.minecraft.entity.Entity
     *
     * @param eyes position of the eyes looking at the angle
     * @param target the target point where they are pointed
     * @return a new Vec2f(yaw, pitch) of the angles the eyes are rotated to look at the target
     */
    public static Vec2f lookVector(@NotNull final Vec3d eyes, @NotNull final Vec3d target) {
        return lookVector(target.subtract(eyes));
    }

    public static Vec2f lookVector(@NotNull final Vec3d target) {
        double xDiff = target.x;
        double yDiff = target.y;
        double zDiff = target.z;
        double horizontal = Math.sqrt(xDiff * xDiff + zDiff * zDiff);

        float pitch = MathHelper.wrapDegrees((float)(MathHelper.atan2(yDiff, horizontal) * -57.2957763671875));
        float yaw = MathHelper.wrapDegrees((float)(MathHelper.atan2(zDiff, xDiff) * 57.2957763671875) - 90.0f);

        return new Vec2f(pitch, yaw);
    }
}
