package io.github.svegon.mclientapi.client.mixin.item;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemLike, FeatureElement, FabricItem {

}
