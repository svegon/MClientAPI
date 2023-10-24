package com.github.svegon.capi.mixininterface;

public interface IPlayerMoveC2SPacket {
    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    double getZ();

    void setZ(double z);

    float getYaw();

    void setYaw(float yaw);

    float getPitch();

    void setPitch(float pitch);

    void setOnGround(boolean onGround);
}
