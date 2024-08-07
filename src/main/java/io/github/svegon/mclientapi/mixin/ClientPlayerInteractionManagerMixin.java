package io.github.svegon.capi.mixin;

import io.github.svegon.capi.MinecraftConstants;
import io.github.svegon.capi.event.interact.ReachDistanceCallback;
import io.github.svegon.capi.event.interact.StopUsingItemListener;
import io.github.svegon.capi.mixininterface.IClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Shadow public abstract float getReachDistance();

    @Inject(method = "getReachDistance", at = @At("RETURN"), cancellable = true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(ReachDistanceCallback.EVENT.invoker().getReachDistance((ClientPlayerInteractionManager)
                (Object) this, cir.getReturnValue()));
    }

    @Inject(method = "stopUsingItem", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V",
            shift = At.Shift.AFTER), cancellable = true)
    private void onStopUsingItem(PlayerEntity player, CallbackInfo ci) {
        StopUsingItemListener.EVENT.invoker().onStoppingUsingItem((ClientPlayerInteractionManager) (Object) this,
                player, ci);
    }

    /**
     * @author Svegon
     * @reason adapt to reach distance changes issued by mods
     */
    @Overwrite
    public boolean hasExtendedReach() {
        return getReachDistance() > MinecraftConstants.CLIENT_REACH_DISTANCE;
    }

    @Override
    public void gameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void previousGameMode(GameMode gameMode) {
        this.previousGameMode = gameMode;
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
