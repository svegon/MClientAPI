package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.mixininterface.MClientAPIPlayerListEntry;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin implements MClientAPIPlayerListEntry {
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
