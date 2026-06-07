package io.github.svegon.mclientapi.client.mixin.blockentity;

import io.github.svegon.mclientapi.client.event.render.tooltip.SpawnerTooltipListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Spawner.class)
public interface SpawnerMixin {
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private static void onAppendHoverText(@Nullable TypedEntityData<BlockEntityType<?>> data,
                                          Consumer<Component> consumer, String nextSpawnDataTagKey, CallbackInfo ci) {
        SpawnerTooltipListener.Companion.getEVENT().invoker().appendSpawnerTooltip(data, (text) -> {
            consumer.accept(text);
            return null;
        }, nextSpawnDataTagKey, ci);
    }
}
