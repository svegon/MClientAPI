package io.github.svegon.capi.mixininterface;

import net.minecraft.village.TradeOffer;

public interface IMerchantInventory {

    void setTradeOffer(TradeOffer tradeOffer);

    int getOfferIndex();

    void setMerchantRewardedExperience(int merchantRewardedExperience);
}
