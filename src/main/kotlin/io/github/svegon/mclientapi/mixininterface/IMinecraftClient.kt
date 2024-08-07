package io.github.svegon.capi.mixininterface

import net.minecraft.client.render.RenderTickCounter

interface IMinecraftClient {
    val windowProvider: WindowProvider?

    fun getRenderTickCounter(): RenderTickCounter?

    fun setRenderTickCounter(renderTickCounter: RenderTickCounter?)

    fun getSearchManager(): SearchManager?

    fun setSearchManager(searchManager: SearchManager?)

    var itemUseCooldown: Int

    var attackCooldown: Int

    val pausedTickDelta: Float

    fun lastMetricsSampleTime(): Long

    fun nextDebugInfoUpdateTime(): Long

    fun getSocialInteractionsToast(): TutorialToast?

    fun setSocialInteractionsToast(toast: TutorialToast?)

    val tickTimeTracker: TickTimeTracker?

    fun attack()

    fun pickItem()

    fun useItem()

    fun progressBlockBreaking()
}
