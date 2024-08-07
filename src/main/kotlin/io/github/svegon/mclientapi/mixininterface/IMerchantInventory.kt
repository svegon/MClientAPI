package io.github.svegon.capi.mixininterface

import net.minecraft.village.TradeOffer

interface IMerchantInventory {
    fun setTradeOffer(tradeOffer: TradeOffer?)

    val offerIndex: Int

    fun setMerchantRewardedExperience(merchantRewardedExperience: Int)
}
