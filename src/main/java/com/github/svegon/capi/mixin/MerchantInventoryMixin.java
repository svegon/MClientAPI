package com.github.svegon.capi.mixin;

import com.github.svegon.capi.mixininterface.IMerchantInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.village.MerchantInventory;
import net.minecraft.village.TradeOffer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MerchantInventory.class)
public abstract class MerchantInventoryMixin implements Inventory, IMerchantInventory {

    @Shadow private @Nullable TradeOffer tradeOffer;

    @Shadow private int offerIndex;

    @Shadow private int merchantRewardedExperience;

    public void setTradeOffer(TradeOffer tradeOffer) {
        this.tradeOffer = tradeOffer;
    }

    public int getOfferIndex() {
        return offerIndex;
    }

    public void setMerchantRewardedExperience(int merchantRewardedExperience) {
        this.merchantRewardedExperience = merchantRewardedExperience;
    }
}
