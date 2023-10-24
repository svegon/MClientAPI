package com.github.svegon.capi.mixininterface;

import net.minecraft.village.Merchant;
import net.minecraft.village.MerchantInventory;

public interface IMerchantScreenHandler {
    Merchant getMerchant();

    void setMerchant(Merchant merchant);

    MerchantInventory getMerchantInventory();

    void setMerchantInventory(MerchantInventory merchantInventory);

    void playYesSoundServer();
}
