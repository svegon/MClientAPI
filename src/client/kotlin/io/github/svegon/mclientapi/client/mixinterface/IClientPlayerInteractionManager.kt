package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.GameMode

interface IClientPlayerInteractionManager {
    var gameMode: GameMode

    var previousGameMode: GameMode

    var currentBreakingPos: BlockPos

    var selectedStack: ItemStack

    var currentBreakingProgress: Float

    var blockBreakingSoundCooldown: Float

    var blockBreakingCooldown: Int

    var breakingBlock: Boolean

    var lastSelectedSlot: Int
}
