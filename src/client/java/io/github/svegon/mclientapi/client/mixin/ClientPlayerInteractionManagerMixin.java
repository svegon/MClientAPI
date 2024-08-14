package io.github.svegon.mclientapi.client.mixin;

import io.github.svegon.mclientapi.client.event.interaction.StopUsingItemListener;
import io.github.svegon.mclientapi.client.mixinterface.IClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {
    @Shadow
    private BlockPos currentBreakingPos;
    @Shadow
    private ItemStack selectedStack;
    @Shadow
    private float currentBreakingProgress;
    @Shadow
    private float blockBreakingSoundCooldown;
    @Shadow
    private int blockBreakingCooldown;
    @Shadow
    private boolean breakingBlock;
    @Shadow
    private GameMode gameMode = GameMode.DEFAULT;
    @Shadow
    @Nullable
    private GameMode previousGameMode;
    @Shadow
    private int lastSelectedSlot;

    @Inject(at = @At("HEAD"), method = "stopUsingItem", cancellable = true)
    private void onStopUsingItem(PlayerEntity player, CallbackInfo ci) {
        StopUsingItemListener.EVENT.invoker().onStoppingUsingItem((ClientPlayerInteractionManager) (Object)
                this, player, ci);
    }

    @NotNull
    @Override
    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @NotNull
    @Override
    public @Nullable GameMode getPreviousGameMode() {
        return previousGameMode;
    }

    @Override
    public void setPreviousGameMode(@Nullable GameMode previousGameMode) {
        this.previousGameMode = previousGameMode;
    }

    @Override
    public BlockPos getCurrentBreakingPos() {
        return currentBreakingPos;
    }

    @Override
    public void setCurrentBreakingPos(BlockPos currentBreakingPos) {
        this.currentBreakingPos = currentBreakingPos;
    }

    @Override
    public int getBlockBreakingCooldown() {
        return blockBreakingCooldown;
    }

    @Override
    public void setBlockBreakingCooldown(int blockBreakingCooldown) {
        this.blockBreakingCooldown = blockBreakingCooldown;
    }

    @Override
    public float getBlockBreakingSoundCooldown() {
        return blockBreakingSoundCooldown;
    }

    @Override
    public void setBlockBreakingSoundCooldown(float blockBreakingSoundCooldown) {
        this.blockBreakingSoundCooldown = blockBreakingSoundCooldown;
    }

    @Override
    public boolean getBreakingBlock() {
        return breakingBlock;
    }

    @Override
    public void setBreakingBlock(boolean breakingBlock) {
        this.breakingBlock = breakingBlock;
    }

    @Override
    public float getCurrentBreakingProgress() {
        return currentBreakingProgress;
    }

    @Override
    public void setCurrentBreakingProgress(float currentBreakingProgress) {
        this.currentBreakingProgress = currentBreakingProgress;
    }

    @NotNull
    @Override
    public ItemStack getSelectedStack() {
        return selectedStack;
    }

    @Override
    public void setSelectedStack(ItemStack selectedStack) {
        this.selectedStack = selectedStack;
    }

    @Override
    public int getLastSelectedSlot() {
        return lastSelectedSlot;
    }

    @Override
    public void setLastSelectedSlot(int lastSelectedSlot) {
        this.lastSelectedSlot = lastSelectedSlot;
    }
}
