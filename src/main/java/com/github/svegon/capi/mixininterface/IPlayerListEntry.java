package com.github.svegon.capi.mixininterface;

import net.minecraft.world.GameMode;

public interface IPlayerListEntry {
    void gameMode(GameMode gameMode);

    void latency(int latency);
}
