package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.toast.TutorialToast
import net.minecraft.client.util.WindowProvider
import net.minecraft.util.profiler.TickTimeTracker

interface IMinecraftClient {
    val windowProvider: WindowProvider

    var itemUseCooldown: Int

    var attackCooldown: Int

    var lastMetricsSampleTime: Long

    var nextDebugInfoUpdateTime: Long

    var socialInteractionsToast: TutorialToast?

    val tickTimeTracker: TickTimeTracker?

    fun attack()

    fun pickItem()

    fun useItem()

    fun progressBlockBreaking()
}
