package io.github.svegon.capi.mixininterface

import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.GameMode

interface IClientPlayerInteractionManager {
    fun gameMode(gameMode: GameMode?)

    fun previousGameMode(gameMode: GameMode?)

    var currentBreakingPos: BlockPos?

    fun getSelectedStack(): ItemStack?

    fun setSelectedStack(stack: ItemStack?)

    var currentBreakingProgress: Float

    var blockBreakingSoundCooldown: Float

    var blockBreakingCooldown: Int

    fun setBreakingBlock(breaking: Boolean)

    var lastSelectedSlot: Int
}
