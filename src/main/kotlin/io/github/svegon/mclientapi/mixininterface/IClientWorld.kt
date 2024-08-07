package io.github.svegon.capi.mixininterface

import net.minecraft.client.network.ClientPlayNetworkHandler

interface IClientWorld {
    val networkHandler: ClientPlayNetworkHandler?

    fun pendingUpdateManager(): PendingUpdateManager?
}
