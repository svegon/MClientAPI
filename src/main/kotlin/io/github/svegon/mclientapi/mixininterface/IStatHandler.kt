package io.github.svegon.mclientapi.mixininterface

import it.unimi.dsi.fastutil.objects.Object2IntMap
import net.minecraft.stat.Stat
import net.minecraft.stat.StatHandler

interface IStatHandler {
    val statMap: Object2IntMap<Stat<*>>

    fun copyFrom(statHandler: StatHandler)
}
