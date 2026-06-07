package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.IPlayerMoveC2SPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerboundMovePlayerPacket.class)
public abstract class PlayerMoveC2SPacketMixin implements Packet<@NotNull ServerGamePacketListener>, IPlayerMoveC2SPacket {
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
    protected float xRot;
    @Shadow
    @Final
    @Mutable
    protected float yRot;
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
        return xRot;
    }

    @Override
    public void setYaw(float xRot) {
        this.xRot = xRot;
    }

    @Override
    public float getPitch() {
        return yRot;
    }

    public void setyRot(float pitch) {
        this.yRot = pitch;
    }

    @Override
    public boolean getOnGround() {
        return onGround;
    }

    @Override
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
