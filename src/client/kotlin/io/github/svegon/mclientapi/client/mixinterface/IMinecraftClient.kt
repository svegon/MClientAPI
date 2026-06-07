package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.components.toasts.TutorialToast

interface IMinecraftClient {
    var `mClientAPI$itemUseCooldown`: Int

    var `mClientAPI$socialInteractionsToast`: TutorialToast?

    val `mClientAPI$tickTimer`: DeltaTracker.Timer

    fun `mClientAPI$attack`()

    fun `mClientAPI$pickItem`()

    fun `mClientAPI$useItem`()

    fun `mClientAPI$progressBlockBreaking`()
}
