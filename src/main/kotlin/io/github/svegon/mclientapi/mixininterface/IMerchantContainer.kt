package io.github.svegon.mclientapi.mixininterface

import net.minecraft.world.item.trading.Merchant
import net.minecraft.world.item.trading.MerchantOffer

interface IMerchantContainer {
    val merchant: Merchant

    fun setTradeOffer(tradeOffer: MerchantOffer?)
}
