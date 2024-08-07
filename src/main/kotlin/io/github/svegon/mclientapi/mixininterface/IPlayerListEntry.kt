package io.github.svegon.capi.mixininterface

import net.minecraft.world.GameMode

interface IPlayerListEntry {
    fun gameMode(gameMode: GameMode?)

    fun latency(latency: Int)
}
