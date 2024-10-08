package io.github.svegon.mclientapi.mixininterface

import net.minecraft.village.Merchant
import net.minecraft.village.TradeOffer

interface IMerchantInventory {
    val merchant: Merchant

    fun setTradeOffer(tradeOffer: TradeOffer?)

    val offerIndex: Int

    fun setMerchantRewardedExperience(merchantRewardedExperience: Int)
}
