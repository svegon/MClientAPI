package io.github.svegon.mclientapi.mixininterface

import net.minecraft.village.Merchant
import net.minecraft.village.MerchantInventory

interface IMerchantScreenHandler {
    val merchant: Merchant

    val merchantInventory: MerchantInventory

    fun playYesSoundAsServer()
}
