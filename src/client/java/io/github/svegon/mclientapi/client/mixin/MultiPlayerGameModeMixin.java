package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.interaction.StopUsingItemListener;
import io.github.svegon.mclientapi.client.mixinterface.IClientPlayerInteractionManager;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin implements IClientPlayerInteractionManager {
    @Shadow
    private BlockPos destroyBlockPos;
    @Shadow
    private float destroyProgress;
    @Shadow
    private int destroyDelay;
    @Shadow
    private boolean isDestroying;
    @Shadow
    private GameType localPlayerMode;
    @Shadow
    @Nullable
    private GameType previousLocalPlayerMode;
    @Shadow
    private int carriedIndex;

    @Inject(at = @At("HEAD"), method = "releaseUsingItem", cancellable = true)
    private void onStopUsingItem(Player player, CallbackInfo ci) {
        StopUsingItemListener.Companion.getEVENT().invoker().onStoppingUsingItem((MultiPlayerGameMode) (Object)
                this, player, ci);
    }

    @NotNull
    @Override
    public GameType getGameMode() {
        return localPlayerMode;
    }

    @Override
    public void setGameMode(GameType GameType) {
        this.localPlayerMode = GameType;
    }

    @NotNull
    @Override
    public GameType getPreviousLocalPlayerMode() {
        return previousLocalPlayerMode;
    }

    @Override
    public void setPreviousLocalPlayerMode(@Nullable GameType previousLocalPlayerMode) {
        this.previousLocalPlayerMode = previousLocalPlayerMode;
    }

    @Override
    public BlockPos getDestroyBlockPos() {
        return destroyBlockPos;
    }

    @Override
    public void setDestroyBlockPos(BlockPos destroyBlockPos) {
        this.destroyBlockPos = destroyBlockPos;
    }

    @Override
    public int getBlockBreakingCooldown() {
        return destroyDelay;
    }

    @Override
    public void setBlockBreakingCooldown(int blockBreakingCooldown) {
        this.destroyDelay = blockBreakingCooldown;
    }

    @Override
    public boolean getDestroying() {
        return isDestroying;
    }

    @Override
    public void setDestroying(boolean destroying) {
        this.isDestroying = destroying;
    }

    @Override
    public float getDestroyProgress() {
        return destroyProgress;
    }

    @Override
    public void setDestroyProgress(float destroyProgress) {
        this.destroyProgress = destroyProgress;
    }

    @Override
    public int getLastSelectedSlot() {
        return carriedIndex;
    }

    @Override
    public void setLastSelectedSlot(int lastSelectedSlot) {
        this.carriedIndex = lastSelectedSlot;
    }
}
