package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.PendingUpdateManager

interface IClientWorld {
    val networkHandler: ClientPlayNetworkHandler

    val pendingUpdateManager: PendingUpdateManager
}
