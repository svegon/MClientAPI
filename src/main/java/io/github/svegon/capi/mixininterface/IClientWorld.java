package io.github.svegon.capi.mixininterface;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PendingUpdateManager;

public interface IClientWorld {
    ClientPlayNetworkHandler getNetworkHandler();

    PendingUpdateManager pendingUpdateManager();
}
