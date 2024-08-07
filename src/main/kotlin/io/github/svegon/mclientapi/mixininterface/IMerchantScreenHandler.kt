package io.github.svegon.capi.mixininterface

import net.minecraft.village.Merchant
import net.minecraft.village.MerchantInventory

interface IMerchantScreenHandler {
    var merchant: Merchant?

    var merchantInventory: MerchantInventory?

    fun playYesSoundServer()
}
