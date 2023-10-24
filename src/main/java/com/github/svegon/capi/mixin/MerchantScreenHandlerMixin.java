package com.github.svegon.capi.mixin;

import com.github.svegon.capi.mixininterface.IMerchantScreenHandler;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.village.Merchant;
import net.minecraft.village.MerchantInventory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MerchantScreenHandler.class)
public abstract class MerchantScreenHandlerMixin extends ScreenHandler implements IMerchantScreenHandler {
    @Mutable
    @Shadow @Final private Merchant merchant;

    @Mutable
    @Shadow @Final private MerchantInventory merchantInventory;

    @Shadow protected abstract void playYesSound();

    private MerchantScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Override
    public Merchant getMerchant() {
        return merchant;
    }

    @Override
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Override
    public MerchantInventory getMerchantInventory() {
        return merchantInventory;
    }

    @Override
    public void setMerchantInventory(MerchantInventory merchantInventory) {
        this.merchantInventory = merchantInventory;
    }

    @Override
    public void playYesSoundServer() {
        playYesSound();
    }
}
