package io.github.svegon.mclientapi.mixininterface

import it.unimi.dsi.fastutil.objects.Object2IntMap
import net.minecraft.stats.Stat
import net.minecraft.stats.StatsCounter

interface IStatsCounter {
    val statMap: Object2IntMap<Stat<*>>

    fun copyFrom(statHandler: StatsCounter)
}
