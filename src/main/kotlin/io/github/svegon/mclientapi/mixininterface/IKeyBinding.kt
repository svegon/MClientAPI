package io.github.svegon.capi.mixininterface

import net.minecraft.client.util.InputUtil

interface IKeyBinding {
    fun boundKey(): InputUtil.Key?

    fun boundKey(key: InputUtil.Key?)

    fun timesPressed(): Int

    fun timesPressed(count: Int)

    val isPressedOverride: Boolean
}
