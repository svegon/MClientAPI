package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.MClientAPIMerchantInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.village.Merchant;
import net.minecraft.village.MerchantInventory;
import net.minecraft.village.TradeOffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MerchantInventory.class)
public abstract class MerchantInventoryMixin implements Inventory, MClientAPIMerchantInventory {
    @Shadow private @Nullable TradeOffer tradeOffer;

    @Shadow private int offerIndex;

    @Shadow private int merchantRewardedExperience;

    @Shadow @Final private Merchant merchant;

    @NotNull
    @Override
    public Merchant getMerchant() {
        return merchant;
    }

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
