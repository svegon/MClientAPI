package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.PendingUpdateManager

interface IClientWorld {
    val client: MinecraftClient

    val networkHandler: ClientPlayNetworkHandler

    val pendingUpdateManager: PendingUpdateManager
}
