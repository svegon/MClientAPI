package io.github.svegon.capi.mixin;

import io.github.svegon.capi.mixininterface.IPlayerMoveC2SPacket;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerMoveC2SPacket.class)
public abstract class PlayerMoveC2SPacketMixin implements Packet<ServerPlayPacketListener>, IPlayerMoveC2SPacket {
    @Shadow
    @Final
    @Mutable
    protected double x;
    @Shadow
    @Final
    @Mutable
    protected double y;
    @Shadow
    @Final
    @Mutable
    protected double z;
    @Shadow
    @Final
    @Mutable
    protected float yaw;
    @Shadow
    @Final
    @Mutable
    protected float pitch;
    @Shadow
    @Final
    @Mutable
    protected boolean onGround;

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
