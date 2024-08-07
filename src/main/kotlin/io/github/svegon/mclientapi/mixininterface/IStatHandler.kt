package io.github.svegon.capi.mixininterface

import it.unimi.dsi.fastutil.objects.Object2IntMap
import net.minecraft.stat.Stat
import net.minecraft.stat.StatHandler

interface IStatHandler {
    @JvmField
    val statMap: Object2IntMap<Stat<*>?>?

    fun copyFrom(statHandler: StatHandler?)
}
