package io.github.svegon.mclientapi.client.mixinterface

import com.mojang.blaze3d.platform.InputConstants

interface IKeyMapping {
    val boundKey: InputConstants.Key

    var timesPressed: Int

    val isReallyPressed: Boolean
}
