package io.github.svegon.capi.mixin;

import io.github.svegon.capi.mixininterface.IPlayerListEntry;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin implements IPlayerListEntry {
    @Override
    public void gameMode(GameMode gameMode) {
        setGameMode(gameMode);
    }

    @Override
    public void latency(int latency) {
        setLatency(latency);
    }

    @Shadow
    protected abstract void setGameMode(GameMode gameMode);


    @Shadow
    protected abstract void setLatency(int latency);
}
