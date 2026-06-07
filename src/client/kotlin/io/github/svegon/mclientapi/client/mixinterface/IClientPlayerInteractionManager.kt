package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameType

interface IClientPlayerInteractionManager {
    var gameMode: GameType

    var previousLocalPlayerMode: GameType

    var destroyBlockPos: BlockPos

    var destroyProgress: Float

    var blockBreakingCooldown: Int

    var destroying: Boolean

    var lastSelectedSlot: Int
}
