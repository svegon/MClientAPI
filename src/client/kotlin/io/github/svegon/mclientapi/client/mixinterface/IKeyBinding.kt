package io.github.svegon.mclientapi.client.mixinterface

import net.minecraft.client.util.InputUtil

interface IKeyBinding {
    val boundKey: InputUtil.Key

    var timesPressed: Int

    val isReallyPressed: Boolean
}
