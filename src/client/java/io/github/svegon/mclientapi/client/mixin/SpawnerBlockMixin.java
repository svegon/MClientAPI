package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.render.tooltip.SpawnerTooltipListener;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SpawnerBlock.class)
public abstract class SpawnerBlockMixin extends BlockWithEntity {
    private SpawnerBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockWithEntity;appendTooltip" +
            "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/List;" +
            "Lnet/minecraft/item/tooltip/TooltipType;)V"), method = "appendTooltip", cancellable = true)
    private void onAppendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip,
                                 TooltipType options, CallbackInfo ci) {
        SpawnerTooltipListener.EVENT.invoker().appendSpawnerTooltip((SpawnerBlock) (Object)
                this, stack, context, tooltip, options, ci);
    }
}
