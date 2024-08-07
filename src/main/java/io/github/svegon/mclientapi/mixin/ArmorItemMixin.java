package io.github.svegon.capi.mixin;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin extends Item implements Equipment {
    private ArmorItemMixin(Settings settings) {
        super(settings);
    }
}
