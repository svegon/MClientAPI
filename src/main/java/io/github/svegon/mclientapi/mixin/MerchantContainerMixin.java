package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.IMerchantContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.MerchantContainer;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MerchantContainer.class)
public abstract class MerchantContainerMixin implements Container, IMerchantContainer {
    @Shadow private @Final Merchant merchant;
    @Shadow private @Nullable MerchantOffer activeOffer;

    @NotNull
    @Override
    public Merchant getMerchant() {
        return merchant;
    }

    public void setTradeOffer(MerchantOffer tradeOffer) {
        this.activeOffer = tradeOffer;
    }
}
